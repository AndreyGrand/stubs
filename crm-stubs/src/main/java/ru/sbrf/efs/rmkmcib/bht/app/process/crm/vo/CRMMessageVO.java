package ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo;

import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmJmsMessageType;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.SendingMessageDTO;

import java.util.Map;

/**
 * Created by sbt-manayev-iye on 03.08.2016.
 *
 */
public class CRMMessageVO implements SendingMessageDTO {
    /**
     * класс с запросом
     */
    private Object request;

    /**
     * сюда будет записан ответ и потом отправиться по jms
     */
    private Object response;

    /**
     * так как приходит по jms в CRM запросах все в xml строках,
     * то здесь хранит конкретные типы для маршаллинга-анмаршаллинга
     */
    private Class reqClass;

    private Class resClass;

    /**
     * если ответ - уже xml строка, то ее не надо маршалить(needsMarshalling = false)
     */
    private boolean isXmlString = true;


    public boolean isXmlString() {
        return isXmlString;
    }

    public void setXmlString(boolean xmlString) {
        isXmlString = xmlString;
    }

    private Map<String,Object> properties;

    private RmKmJmsMessageType type = RmKmJmsMessageType.XML;

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public RmKmJmsMessageType getType() {
        return type;
    }

    public CRMMessageVO(Object request, Object response, Class reqClass, Class resClass, Map<String, Object> properties) {
        this.request = request;
        this.response = response;
        this.reqClass = reqClass;
        this.resClass = resClass;
        this.properties = properties;
    }

    public void setType(RmKmJmsMessageType type) {
        this.type = type;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Class getReqClass() {
        return reqClass;
    }

    public void setReqClass(Class reqClass) {
        this.reqClass = reqClass;
    }

    public Class getResClass() {
        return resClass;
    }

    public void setResClass(Class resClass) {
        this.resClass = resClass;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
