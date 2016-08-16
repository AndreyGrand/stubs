package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;
import ru.sbrf.efs.rmkmcib.bht.app.util.loader.ResourceAccessor;

/**
 * Created by sbt-manayev-iye on 03.08.2016.
 *
 * получение данных из mock-xml
 */
@Service("GetResponseFileStep")
public class GetResponseFileStep extends AbstractStep implements ProcessStep  {

    private static final Logger log = LoggerFactory.getLogger(GetResponseFileStep.class);

    @Autowired
    private ResourceAccessor resourceAccessor;

    @Autowired
    private JaxbManager jaxbManager;

    /**
     * по классу ответа из message выбирает готовую xml с ответом из ресурсов и подставляет как ответ
     *
     * @param exchange - приходит от Camel, оригинальный запрос
     * @param message  - данные для преобразования
     * @return тот-же message, но с ответом-xml строкой из соответствующего файла
     */
    public CRMMessageVO process(Exchange exchange, CRMMessageVO message) throws CRMMessageProcessingException {
        String resourceKey = "classpath:".concat("mockfiles/").concat(message.getResClass().getSimpleName().toLowerCase()).concat(".xml");
        String resp = appendRqUID(resourceAccessor.readResourceAsString(resourceKey),
                message);
        message.setResponse(resp);
        message.setXmlString(true);
        return message;
    }

}
