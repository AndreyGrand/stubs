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
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.GetLegalClientManagerTaskDetailsRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.MessageType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rs.GetLegalClientManagerTaskDetailsRsType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRsType;
import ru.sbrf.efs.rmkmcib.bht.TestUtils;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmBHTJmsSender;
import ru.sbrf.efs.rmkmcib.bht.app.mocks.MockJmsTemplate;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessRoutingStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter.MessagesConverter;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;
import ru.sbrf.efs.rmkmcib.bht.app.util.loader.ResourceAccessor;
import ru.sbrf.efs.rmkmcib.bht.config.RmKmAppConfig;
import ru.sbrf.rmkmbh.database.config.RmKmDatabaseConfig;
import ru.sbrf.rmkmbh.database.repository.TaskDetailsRepository;
import ru.sbrf.rmkmbh.database.repository.TaskListRepository;
import ru.sbrf.ufs.kmcib.entity.TaskDetails;
import ru.sbrf.ufs.kmcib.entity.TaskList;

import javax.xml.bind.JAXBException;
import java.util.Map;

/**
 * Created by sbt-manayev-iye on 11.08.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RmKmAppConfig.class)
public class CRMDatabaseMockRouteTest extends CamelTestSupport {

    @Autowired
    @Qualifier("CRMMessageConverter")
    private MessagesConverter converter;

    @Autowired
    @Qualifier("CRMDatabaseActionProcessStep")
    private ProcessRoutingStep getFromDbStep;

    @Autowired
    private RmKmBHTJmsSender jmsSender;

    @Autowired
    private JaxbManager jaxbManager;

    @Before
    public void appendMockingSender(){
        TestUtils.setPrivateField("jmsTemplate",jmsSender,mockJmsTemplate);
    }

    MockJmsTemplate mockJmsTemplate = new MockJmsTemplate();

    private final String inRoute = "direct:start1";

    private final String outRoute = "direct:end1";

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private ResourceAccessor resourceAccessor;

    @Autowired
    private TaskDetailsRepository taskDetailsRepository;

    @Produce(uri = inRoute)
    private  ProducerTemplate template;

    @Test
    @Transactional
    public void testCRMRouteSrvGetLegalClientTaskDetails() throws InterruptedException, ClassNotFoundException, CRMMessageProcessingException, JAXBException {
        final GetLegalClientManagerTaskDetailsRqType tt = new GetLegalClientManagerTaskDetailsRqType();
        tt.setRqTm("test");
        tt.setMethod("test");
        tt.setRqUID("test");
        MessageType mt = new MessageType();
        mt.setLogin("test-login");
        tt.setMessage(mt);
        //убираем лищние переносы строк, пробелы и прочее, чтобы equals в конце не свалился из-за них
        GetLegalClientManagerTaskDetailsRsType original = jaxbManager.unmarshall(resourceAccessor.readResourceAsString("classpath:mockfiles/getlegalclientmanagertaskdetailsrstype.xml"),
                GetLegalClientManagerTaskDetailsRsType.class);
        final Map<String,Object> props = TestUtils.getProps("GetLegalClientManagerTaskDetailsRq","SrvGetLegalClientManagerTaskDetails");
        original.setRqUID(props.get(CRMMessageHeaders.RQ_UID_HEADER.getName()).toString());
        String doc =jaxbManager.marshall(original);
        TaskDetails tempResp = new TaskDetails();
        tempResp.setMessageLogin(mt.getLogin());
        tempResp.setResponse(null); // ToDo tempResp.setResponse(doc);
        taskDetailsRepository.save(tempResp);
        GetLegalClientManagerTaskDetailsRsType compareTo = jaxbManager.unmarshall(doSend(tt, props), GetLegalClientManagerTaskDetailsRsType.class);
        assert compareTo.getRqUID().equals(original.getRqUID());
        assert compareTo.getStatus().getStatusDesc().equals(original.getStatus().getStatusDesc());
    }

    @Test
    @Transactional
    public void testCRMRouteSrvGetLegalClientManagerTaskList() throws InterruptedException, ClassNotFoundException, CRMMessageProcessingException, JAXBException {
        final GetlegalClientManagerTaskListRqType tt = new GetlegalClientManagerTaskListRqType();
        tt.setRqTm("test");
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setUserLogin("test-login");
        //убираем лищние переносы строк, пробелы и прочее, чтобы equals в конце не свалился из-за них
        GetlegalClientManagerTaskListRsType original = jaxbManager.unmarshall(resourceAccessor.readResourceAsString("classpath:mockfiles/getlegalclientmanagertasklistrstype.xml"),
                GetlegalClientManagerTaskListRsType.class);
        final Map<String,Object> props = TestUtils.getProps("GetlegalClientManagerTaskListRq","SrvGetLegalClientManagerTaskList");
        original.setRqUID(props.get(CRMMessageHeaders.RQ_UID_HEADER.getName()).toString());
        String doc =jaxbManager.marshall(original);
        TaskList tempResp = new TaskList();
        tempResp.setUserlogin(tt.getUserLogin());
        tempResp.setResponse(null);// ToDo tempResp.setResponse(doc);
        taskListRepository.save(tempResp);
        GetlegalClientManagerTaskListRsType compareTo = jaxbManager.unmarshall(doSend(tt, props), GetlegalClientManagerTaskListRsType.class);
        assert compareTo.getRqUID().equals(original.getRqUID());
        assert compareTo.getStatus().getStatusDesc().equals(original.getStatus().getStatusDesc());
    }

    public String doSend(Object payload, Map<String,Object> props) throws InterruptedException, JAXBException, ClassNotFoundException, CRMMessageProcessingException {
        template.sendBodyAndHeaders(jaxbManager.marshall(payload), props);
        Thread.sleep(500L);
        assert mockJmsTemplate.getCounter() == 1;
        return mockJmsTemplate.getText();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(inRoute).bean(converter).bean(getFromDbStep).bean(jmsSender, "sendMessage(*,".concat(outRoute).concat(")"));
            }
        };
    }
}
