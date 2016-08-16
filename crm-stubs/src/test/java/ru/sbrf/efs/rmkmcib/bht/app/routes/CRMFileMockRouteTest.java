package ru.sbrf.efs.rmkmcib.bht.app.routes;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientList.dto.rq.GetLegalClientListRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.GetLegalClientManagerTaskDetailsRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientProfile.dto.rq.GetLegalClientProfileRqType;
import ru.sbrf.efs.integration.esb.fs.srvPutLegalClientManagerTask.dto.rq.PutLegalClientManagerTaskRqType;
import ru.sbrf.efs.rmkmcib.bht.TestUtils;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmBHTJmsSender;
import ru.sbrf.efs.rmkmcib.bht.app.mocks.MockJmsTemplate;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessRoutingStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter.MessagesConverter;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;
import ru.sbrf.efs.rmkmcib.bht.app.util.loader.ResourceAccessor;
import ru.sbrf.efs.rmkmcib.bht.config.RmKmAppConfig;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

/**
 * при изменении CRMMockRoute менять route в тестах
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RmKmAppConfig.class)
public class CRMFileMockRouteTest extends CamelTestSupport{

    @Autowired
    private ResourceAccessor resourceAccessor;

    @Autowired
    @Qualifier("CRMMessageConverter")
    private MessagesConverter converter;

    @Autowired
    private JaxbManager jaxbManager;

    @Autowired
    @Qualifier("GetResponseFileStep")
    private ProcessStep getFileStep;

    @Autowired
    @Qualifier("CRMDatabaseActionProcessStep")
    private ProcessRoutingStep getFromDbStep;

    @Autowired
    private RmKmBHTJmsSender jmsSender;

    private MockJmsTemplate mockJmsTemplate = new MockJmsTemplate();

    private final String inRoute = "direct:start";
    private final String outRoute = "direct:end";

    @Override
    public boolean isDumpRouteCoverage() {
        return true;
    }

    @Produce(uri = inRoute)
    private ProducerTemplate template;

    @Before
    public void appendMockingSender(){
        TestUtils.setPrivateField("jmsTemplate",jmsSender,mockJmsTemplate);
    }

    @Test
    public void testPutLegalClientManagerTaskRq_UndefProps() throws JAXBException, ClassNotFoundException, InterruptedException, CRMMessageProcessingException {
        Document doc = jaxbManager.getDocFromXmlString(resourceAccessor.readResourceAsString( "classpath:mockfiles/putlegalclientmanagertaskrstype.xml"));
        jaxbManager.setTagTextValue(doc,CRMMessageHeaders.RQ_UID_HEADER.getName(),"2093b67dcabc47dabec4b3118bc19d65");
        doSendNoProps(TestUtils.PutLegalClientManagerTaskRq, jaxbManager.docToString(doc));
    }

    public void doSendNoProps(String payload,String comparator) throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        template.sendBodyAndHeaders(payload, new HashMap<String, Object>());
        Thread.sleep(500L);
        assert mockJmsTemplate.getCounter() == 1;
        assert mockJmsTemplate.getText().equals(comparator);
    }

    public void doSend(Object payload,String operationName,String serviceName,String mockFile) throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final Map<String,Object> props = TestUtils.getProps(operationName,serviceName);
        template.sendBodyAndHeaders(jaxbManager.marshall(payload), props);
        Thread.sleep(500L);
        assert mockJmsTemplate.getCounter() == 1;
        Document doc = jaxbManager.getDocFromXmlString(resourceAccessor.readResourceAsString(mockFile));
        jaxbManager.setTagTextValue(doc, CRMMessageHeaders.RQ_UID_HEADER.getName(),props.get(CRMMessageHeaders.RQ_UID_HEADER.getName()).toString());
        assert mockJmsTemplate.getText().equals(jaxbManager.docToString(doc));
    }

    @Test
    public void testCRMRouteSrvGetLegalClientManagerTaskDetails() throws JAXBException, ClassNotFoundException, InterruptedException, CRMMessageProcessingException {
        final GetLegalClientManagerTaskDetailsRqType tt = new GetLegalClientManagerTaskDetailsRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        doSend(tt,"GetLegalClientManagerTaskDetailsRq","SrvGetLegalClientManagerTaskDetails","classpath:mockfiles/getlegalclientmanagertaskdetailsrstype.xml");
    }
    @Test
    public void testCRMRouteSrvGetLegalClientProfile() throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final GetLegalClientProfileRqType tt = new GetLegalClientProfileRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        doSend(tt,"GetLegalClientProfileRq","SrvGetLegalClientProfile","classpath:mockfiles/getlegalclientprofilerstype.xml");
    }
    @Test
    public void testCRMRouteSrvGetLegalClientProfileLC() throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final GetLegalClientProfileRqType tt = new GetLegalClientProfileRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        doSend(tt,"GetLegalClientProfileRq","srvgetLegalClientProfile","classpath:mockfiles/getlegalclientprofilerstype.xml");
    }
    @Test
    public void testCRMRouteSrvGetLegalClientList() throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final GetLegalClientListRqType tt = new GetLegalClientListRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        doSend(tt,"GetLegalClientListRq","SrvGetLegalClientList","classpath:mockfiles/getlegalclientlistrstype.xml");
    }
    @Test
    public void testCRMRouteSrvGetLegalClientManagerTaskList() throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final GetlegalClientManagerTaskListRqType tt = new GetlegalClientManagerTaskListRqType();
        tt.setRqTm("test");
        tt.setMethod("test");
        tt.setRqUID("test");
        doSend(tt,"GetlegalClientManagerTaskListRq","SrvGetLegalClientManagerTaskList","classpath:mockfiles/getlegalclientmanagertasklistrstype.xml");
    }
    @Test
    public void testCRMRouteSrvPutLegalClientManagerTask() throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final PutLegalClientManagerTaskRqType tt = new PutLegalClientManagerTaskRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        doSend(tt,"PutLegalClientManagerTaskRq","SrvPutLegalClientManagerTask","classpath:mockfiles/putlegalclientmanagertaskrstype.xml");
    }
    @Test
    public void testCRMRouteSrvPutLegalClientManagerTaskLC() throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        final PutLegalClientManagerTaskRqType tt = new PutLegalClientManagerTaskRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        doSend(tt,"PutLegalClientManagerTaskRq","srvputLegalClientManagertask","classpath:mockfiles/putlegalclientmanagertaskrstype.xml");
    }
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(inRoute).bean(converter).bean(getFileStep).bean(jmsSender, "sendMessage(*,".concat(outRoute).concat(")"));
            }
        };
    }
}