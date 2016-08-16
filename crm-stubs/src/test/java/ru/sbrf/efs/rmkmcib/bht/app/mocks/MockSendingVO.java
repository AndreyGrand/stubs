package ru.sbrf.efs.rmkmcib.bht.app.mocks;

import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmJmsMessageType;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.SendingMessageDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-manayev-iye on 04.08.2016.
 *
 */
public class MockSendingVO implements SendingMessageDTO {

    private Object response = new Object();
    private boolean isXmlString = false;
    public MockSendingVO(Object response) {
        this.response = response;
    }

    @Override
    public boolean isXmlString() {
        return isXmlString;
    }

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<>();
    }

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public RmKmJmsMessageType getType() {
        return RmKmJmsMessageType.TEXT;
    }

}
