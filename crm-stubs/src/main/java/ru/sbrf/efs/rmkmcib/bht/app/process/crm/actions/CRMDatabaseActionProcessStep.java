package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.db.CRMBoundedClassesProcessor;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-manayev-iye on 10.08.2016.
 *
 * @see ProcessRoutingStep
 */
@Service("CRMDatabaseActionProcessStep")
public class CRMDatabaseActionProcessStep implements ProcessRoutingStep {

    private final Class<? extends Annotation> processSelectorAnnotation = CRMBoundedClassesProcessor.class;

    private Map<String,ProcessStep> availableProcesses = new HashMap<>();

    @Override
    public CRMMessageVO process(Exchange exchange, CRMMessageVO message) throws CRMMessageProcessingException {
        String currentKey = message.getReqClass().getName().concat(message.getResClass().getName());
        if(availableProcesses.containsKey(currentKey)){
            message = availableProcesses.get(currentKey).process(exchange,message);
            exchange.getIn().setHeader(CRMMessageHeaders.CRM_DB_ACTION_MISS.getName(),Boolean.FALSE);
        }else{
            exchange.getIn().setHeader(CRMMessageHeaders.CRM_DB_ACTION_MISS.getName(),Boolean.TRUE);
        }
        return message;
    }

    public Map<String, ProcessStep> getAvailableProcesses() {
        return availableProcesses;
    }

    public void setAvailableProcesses(Map<String, ProcessStep> availableProcesses) {
        this.availableProcesses = availableProcesses;
    }

    @Override
    public Class<? extends Annotation> getProcessSelectorAnnotation() {
        return processSelectorAnnotation;
    }
}
