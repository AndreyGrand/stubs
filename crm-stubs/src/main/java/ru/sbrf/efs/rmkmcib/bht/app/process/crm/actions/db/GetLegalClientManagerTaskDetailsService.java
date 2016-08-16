package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.db;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.GetLegalClientManagerTaskDetailsRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rs.ActionInfoType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rs.GetLegalClientManagerTaskDetailsRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rs.MessageType;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.AbstractStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;
import ru.sbrf.efs.rmkmcib.bht.app.util.DBUtils;
import ru.sbrf.rmkmbh.database.repository.TaskDetailsRepository;
import ru.sbrf.ufs.kmcib.entity.TaskDetails;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by sbt-manayev-iye on 11.08.2016.
 */
@CRMBoundedClassesProcessor(requestClass = GetLegalClientManagerTaskDetailsRqType.class,responseClass = GetLegalClientManagerTaskDetailsRsType.class)
@Service("GetLegalClientManagerTaskDetailsService")
public class GetLegalClientManagerTaskDetailsService   extends AbstractStep implements ProcessStep {

    private static final Logger log = LoggerFactory.getLogger(GetLegalClientManagerTaskDetailsService.class);

    @Autowired
    private TaskDetailsRepository taskDetailsRepository;

    @Override
    public CRMMessageVO process(Exchange exchange, CRMMessageVO message) throws CRMMessageProcessingException {
        try {
            GetLegalClientManagerTaskDetailsRqType req;
            boolean responseFound = false;
            if(message.isXmlString()){
                req = jaxbManager.unmarshall(message.getRequest().toString(),GetLegalClientManagerTaskDetailsRqType.class);
            }else{
                //гарантию безопасного преобразования должна предоставлять маршрутизация
                req = (GetLegalClientManagerTaskDetailsRqType) message.getResponse();
            }
            if(req.getMessage() != null && !StringUtils.isEmpty(req.getMessage().getLogin())){
                List<TaskDetails> ent = taskDetailsRepository.findByMessageLogin(req.getMessage().getLogin());
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
    private GetLegalClientManagerTaskDetailsRsType generateEmptyResponse(CRMMessageVO message){
        GetLegalClientManagerTaskDetailsRsType temp = new GetLegalClientManagerTaskDetailsRsType();
        MessageType tempMessage = new MessageType();
        tempMessage.setActionInfo(new ActionInfoType());
        temp.setMessage(tempMessage);
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
