package ru.sbrf.efs.rmkmcib.bht.app.jms.sender;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import ru.sbrf.efs.rmkmcib.bht.TestUtils;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.mocks.MockJmsTemplate;
import ru.sbrf.efs.rmkmcib.bht.app.mocks.MockSendingVO;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.Map;

public class RmKmBHTJmsSenderImplTest {

    private RmKmBHTJmsSenderImpl sender;

    @Before
    public void initImpl(){
        sender = new RmKmBHTJmsSenderImpl();
        sender.setJaxbManager(new JaxbManager() {
            @Override
            public String docToString(Document doc) throws CRMMessageProcessingException {
                return null;
            }

            @Override
            public void setTagTextValue(Document doc, String tag, String value) {

            }

            @Override
            public String getOperationName(Document doc) {
                return null;
            }

            @Override
            public Marshaller getMarshaller(String key) throws JAXBException, ClassNotFoundException {
                return null;
            }

            @Override
            public String marshall(Object payload) throws JAXBException, ClassNotFoundException {
                return payload.toString();
            }
            @SuppressWarnings("unchecked")
            @Override
            public <T> T unmarshall(String payload, Class<T> clazz) throws JAXBException, ClassNotFoundException {
                return (T) payload;
            }

            @Override
            public Document getDocFromXmlString(String xml) throws CRMMessageProcessingException {
                return null;
            }

            @Override
            public Map<String, String> getFirstMatchingTagTextContent(Document doc, String[] tags) {
                return null;
            }
        });
    }

    @Test
    public void testSender(){
        MockJmsTemplate temp = new MockJmsTemplate();
        TestUtils.setPrivateField("jmsTemplate",sender,temp);

        sender.sendMessage(null,null);
        assert temp.getCounter() == 0;

        sender.sendMessage(new MockSendingVO(null),null);
        assert temp.getCounter() == 0;

        sender.sendMessage(new MockSendingVO("test"),"test");
        assert temp.getText().equals("test");
    }

}