package ru.sbrf.efs.rmkmcib.bht.app.mocks;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import ru.sbrf.efs.rmkmcib.bht.TestUtils;

import javax.jms.Destination;

/**
 * Created by sbt-manayev-iye on 04.08.2016.
 */
public class MockJmsTemplate extends JmsTemplate {

    int counter;

    String text;

    public MockJmsTemplate() {
    }

    public String getText() {
        return text;
    }

    @Override
    public void send(Destination destination, MessageCreator messageCreator) throws JmsException {
        //ignore
        counter++;
        text = TestUtils.getPrivateFieldValue("message", messageCreator).toString();
    }

    public int getCounter(){
        return counter;
    }

}