package ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmBHTJmsSender;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manaev on 8/2/16.
 *
 */
@Service("CRMMessageConverter")
public class CRMMessagesConverter implements MessagesConverter {

    private static final Logger log = LoggerFactory.getLogger(CRMMessagesConverter.class);

    private final String headersArray[] = new String[]{CRMMessageHeaders.SERVICE_NAME_HEADER.getName(),
            CRMMessageHeaders.OPERATION_NAME_HEADER.getName(),
            CRMMessageHeaders.RQ_TM_HEADER.getName(),
            CRMMessageHeaders.SERVICE_NAMESPACE_HEADER.getName(),
            CRMMessageHeaders.SC_NAME_HEADER.getName(),
            CRMMessageHeaders.RQ_UID_HEADER.getName()};

    @Autowired
    private CRMClassBounds messageToClass;

    @Autowired
    private RmKmBHTJmsSender rmKmBHTJmsSender;

    @Autowired
    private JaxbManager jaxbManager;

    /**
     * по свойствам ServiceName и OperationName через CRMClassBounds - определяем какие классы нужны для ответа и запроса
     * ВАЖНО! Копирует только хидеры, указанные в headersArray
     *
     * @param exchange приходит от Camel
     * @return объект для дальнейшей обработки
     */
    public CRMMessageVO process(Exchange exchange) throws CRMMessageProcessingException {
        Object body = exchange.getIn().getBody();
        Map<String, Object> properties = new HashMap<>();
        Map<String,Object> temp = exchange.getIn().getHeaders();
        log.debug("Incoming message for route");
        log.debug(body.toString());
        for (String header : headersArray) {
            if (temp.containsKey(header)) {
                properties.put(header, temp.get(header));
                log.debug(header.concat("  ").concat(String.valueOf(temp.get(header))));
            }
        }
        /**
         * TODO сейчас понимаем входящее сообщение как xml строчку - нужно подставить соответсвующие проверки
         */
        Document parsedBody = null;
        /**
         * в хидерах сообщения нет RqUID - значит ответ сервиса не будет обработан, надо добавить
         */
        if(StringUtils.isEmpty(properties.get(CRMMessageHeaders.RQ_UID_HEADER.getName()))){
            parsedBody = jaxbManager.getDocFromXmlString(body.toString());
            //    TODO refactor!!!
            properties.put(CRMMessageHeaders.RQ_UID_HEADER.getName(),jaxbManager.getFirstMatchingTagTextContent(parsedBody,
                    new String[]{CRMMessageHeaders.RQ_UID_HEADER.getName()}).get(CRMMessageHeaders.RQ_UID_HEADER.getName()));
        }
        /**
         * получение классов запросов - ответов через парсинг xml - первый тэг - это имя операции
         */
        if(StringUtils.isEmpty(properties.get(CRMMessageHeaders.OPERATION_NAME_HEADER.getName()))){
            properties.put(CRMMessageHeaders.OPERATION_NAME_HEADER.getName(),
                    jaxbManager.getOperationName(parsedBody));
        }

        CRMClassBoundsImpl.ReqResClass boundClasses;
        if(StringUtils.isEmpty(properties.get(CRMMessageHeaders.SERVICE_NAME_HEADER.getName()))){
            properties.put(CRMMessageHeaders.SERVICE_NAME_HEADER.getName(),messageToClass.getBoundServiceName(properties.get(CRMMessageHeaders.OPERATION_NAME_HEADER.getName()).toString()));
            boundClasses = messageToClass.getBoundClasses(properties.get(CRMMessageHeaders.OPERATION_NAME_HEADER.getName()).toString());
            return new CRMMessageVO(body, null, boundClasses.getReqClass(), boundClasses.getRespClass(), properties);
        }else{
            boundClasses = messageToClass.getBoundClasses(properties.get(CRMMessageHeaders.SERVICE_NAME_HEADER.getName()).toString(),
                    properties.get(CRMMessageHeaders.OPERATION_NAME_HEADER.getName()).toString());
            return new CRMMessageVO(body, null, boundClasses.getReqClass(), boundClasses.getRespClass(), properties);
        }

    }

}
