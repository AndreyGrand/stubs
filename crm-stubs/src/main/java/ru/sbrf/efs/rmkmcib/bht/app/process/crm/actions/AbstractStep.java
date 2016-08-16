package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;

/**
 * Created by sbt-manayev-iye on 11.08.2016.
 * <p>
 * шаг - предок для всех процессов
 */
public abstract class AbstractStep {

    @Autowired
    protected JaxbManager jaxbManager;

    public String appendRqUID(String resp, CRMMessageVO message) throws CRMMessageProcessingException {
        Document doc = jaxbManager.getDocFromXmlString(resp);
        jaxbManager.setTagTextValue(doc, CRMMessageHeaders.RQ_UID_HEADER.getName(),
                message.getProperties().get(CRMMessageHeaders.RQ_UID_HEADER.getName()).toString());
        return jaxbManager.docToString(doc);
    }
}
