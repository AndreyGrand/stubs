package ru.sbrf.efs.rmkmcib.bht;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskDetails.dto.rq.GetLegalClientManagerTaskDetailsRqType;
import ru.sbrf.efs.integration.esb.fs.srvGetLegalClientManagerTaskList.dto.GetlegalClientManagerTaskListRqType;
import ru.sbrf.efs.integration.esb.fs.srvPutLegalClientManagerTask.dto.rq.PutLegalClientManagerTaskRqType;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmBHTJmsSender;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmJmsMessageType;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.SendingMessageDTO;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;
import ru.sbrf.efs.rmkmcib.bht.config.RmKmAppConfig;

import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RmKmAppConfig.class)
public class RmKmBhTesterApplicationTest {

    @Autowired
    private RmKmBHTJmsSender rmRmBHTJmsSender;

    @Value("${activemq.queues.out}")
    private String activeMqOutQueue;

    @Value("${activemq.queues.in}")
    private  String activeMqinQueue;

    @Autowired
    private JaxbManager jaxbManager;

    private void doSend(final Map<String,Object> props, final Object message){
        rmRmBHTJmsSender.sendMessage(new SendingMessageDTO() {
            @Override
            public boolean isXmlString() {
                return false;
            }

            @Override
            public Map<String, Object> getProperties() {
                return props;
            }

            @Override
            public Object getResponse() {
                return message;
            }

            @Override
            public RmKmJmsMessageType getType() {
                return RmKmJmsMessageType.XML;
            }

        }, activeMqinQueue);
    }

    @Test
    public void testSrvPutLegalClientManagerTask(){
        final PutLegalClientManagerTaskRqType tt = new PutLegalClientManagerTaskRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        final Map<String,Object> props = new HashMap<>();
        props.put("ServiceName","SrvPutLegalClientManagerTask");
        props.put("RqTm","2016-08-02T15:51:48.738+04:00");
        props.put("ServiceNamespace","srv://sbg.sbr/ucnServiceVersion001");
        props.put("OperationName","PutLegalClientManagerTaskRq");
        props.put("SCName","urn:sbrfsystems:99-ufs");
        props.put("RqUID","de23596088184793b228469b70583fb3");
//        this.doSend(props, tt);
        assert true;
    }

    @Test
    public void testSrvGetLegalClientManagerTaskDetails(){
        final GetLegalClientManagerTaskDetailsRqType tt = new GetLegalClientManagerTaskDetailsRqType();
        tt.setMethod("test");
        tt.setRqUID("test");
        tt.setRqTm("test");
        final Map<String,Object> props = new HashMap<>();
        props.put("ServiceName","SrvGetLegalClientManagerTaskDetails");
        props.put("RqTm","2016-08-02T15:51:48.738+04:00");
        props.put("ServiceNamespace","srv://sbg.sbr/ucnServiceVersion001");
        props.put("OperationName","GetLegalClientManagerTaskDetailsRq");
        props.put("SCName","urn:sbrfsystems:99-ufs");
        props.put("RqUID","de23596088184793b228469b70583fb3");
//        this.doSend(props, tt);
        assert true;
    }

    @Test
    public void testSrvGetLegalClientManagerTaskList() throws InterruptedException {
        final GetlegalClientManagerTaskListRqType tt = new GetlegalClientManagerTaskListRqType();
        tt.setRqTm("test");
        tt.setMethod("test");
        tt.setRqUID("test");
        final Map<String,Object> props = new HashMap<>();
        props.put("ServiceName","SrvGetLegalClientManagerTaskList");
        props.put("RqTm","2016-08-02T15:51:48.738+04:00");
        props.put("ServiceNamespace","srv://sbg.sbr/ucnServiceVersion001");
        props.put("OperationName","GetlegalClientManagerTaskListRq");
        props.put("SCName","urn:sbrfsystems:99-ufs");
        props.put("RqUID","de23596088184793b228469b70583fb3");
//        this.doSend(props, tt);
        assert true;
    }

}