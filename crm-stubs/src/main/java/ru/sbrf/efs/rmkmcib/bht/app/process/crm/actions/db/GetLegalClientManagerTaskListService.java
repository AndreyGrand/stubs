package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.db;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.SeverityType;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.AbstractStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;
import ru.sbrf.efs.rmkmcib.bht.app.util.DBUtils;
import ru.sbrf.rmkmbh.database.repository.TaskListRepository;
import ru.sbrf.ufs.kmcib.entity.TaskList;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by sbt-manayev-iye on 10.08.2016.
 *
 * Класс для обработки запроса в базе данных, здесь выполняется логика по получению ответа в зависимости от запроса
 * @see CRMBoundedClassesProcessor
 */
@CRMBoundedClassesProcessor(requestClass = GetlegalClientManagerTaskListRqType.class,responseClass = GetlegalClientManagerTaskListRsType.class)
@Service("GetLegalClientManagerTaskListService")
public class GetLegalClientManagerTaskListService  extends AbstractStep implements ProcessStep {

    private static final Logger log = LoggerFactory.getLogger(GetLegalClientManagerTaskListService.class);

    @Autowired
    private TaskListRepository taskListRepository;

    /**
     *
     * @param exchange - приходит от Camel, оригинальный запрос
     * @param message - данные для преобразования
     * @return ответ для обработки
     * @throws CRMMessageProcessingException в оригинале, или JAXBException или ClassNotFoundException
     */
    @Override
    public CRMMessageVO process(Exchange exchange, CRMMessageVO message) throws CRMMessageProcessingException {
        try {
            GetlegalClientManagerTaskListRqType req;
            boolean responseFound = false;
            if(message.isXmlString()){
                req = jaxbManager.unmarshall(message.getRequest().toString(),GetlegalClientManagerTaskListRqType.class);
            }else{
                //гарантию безопасного преобразования должна предоставлять маршрутизация
                req = (GetlegalClientManagerTaskListRqType) message.getResponse();
            }
            if(!StringUtils.isEmpty(req.getUserLogin())){
                List<TaskList> ent = taskListRepository.findByUserlogin(req.getUserLogin());
                if(ent != null && ent.size() > 0){
                    message.setResponse(appendRqUID(DBUtils.clobToString(ent.get(0).getResponse()), message));
                    message.setXmlString(true);
                    responseFound = true;
                }
            }
            if(!responseFound){
                message.setResponse(generateEmptyResponse(message));
                message.setXmlString(false);
            }

        } catch (JAXBException | ClassNotFoundException e) {
            log.error(e.getMessage(),e);
            throw new CRMMessageProcessingException(e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param message входящее сообщение для постороения пустового ответа - оттуда берется RqUID
     * @return пустой ответ
     */
    private GetlegalClientManagerTaskListRsType generateEmptyResponse(CRMMessageVO message){
        GetlegalClientManagerTaskListRsType temp = new GetlegalClientManagerTaskListRsType();
        temp.setListOfActionInfo(new GetlegalClientManagerTaskListRsType.ListOfActionInfo());
        GetlegalClientManagerTaskListRsType.Status status = new GetlegalClientManagerTaskListRsType.Status();
        status.setServerStatusCode("ServerStatusCode");
        status.setSeverity(SeverityType.WARN);
        status.setStatusCode(0L);
        status.setStatusDesc("StatusDesc");
        temp.setStatus(status);
        temp.setMethod("IANUAGetEmployeeListResponse");
        temp.setSPName("urn:sbrfsystems:99-crmorg");
        temp.setSystemId("urn:sbrfsystems:99-ufs");
        /**
         * сразу подставляем RqUid, чтобы сообщение дошло до отправителя
         */
        temp.setRqUID(message.getProperties().get(CRMMessageHeaders.RQ_UID_HEADER.getName()).toString());
        temp.setRqTm(message.getProperties().get(CRMMessageHeaders.RQ_TM_HEADER.getName()).toString());
        return temp;
    }
}
