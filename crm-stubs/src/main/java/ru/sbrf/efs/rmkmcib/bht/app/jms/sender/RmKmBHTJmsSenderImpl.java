package ru.sbrf.efs.rmkmcib.bht.app.jms.sender;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.util.JaxbManager;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sbt-manayev-iye on 02.08.2016.
 *
 * отправка сообщении в очереди к ActiveMQ
 */
@Service
public class RmKmBHTJmsSenderImpl implements RmKmBHTJmsSender {

    private static final Set<String> CUSTOM_HEADERS = new HashSet<>(Arrays.asList(CRMMessageHeaders.CRM_DB_ACTION_MISS.getName()));

    private static final Logger log = LoggerFactory.getLogger(RmKmBHTJmsSenderImpl.class);

    /**
     * кэш очередей
     */
    private ConcurrentHashMap<String,Queue> queues = new ConcurrentHashMap<>();

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JaxbManager jaxbManager;

    private Queue selectQueue(String queue){
        Queue qqueue = queues.get(queue);
        if(qqueue == null ){
            qqueue = queues.putIfAbsent(queue,new ActiveMQQueue(queue));
            if(qqueue == null){
                qqueue = queues.get(queue);
            }
        }
        return qqueue;
    }

    /**
     * выбирает тип сообщения - XML, TEXT и отправляет, для XML понимает нужен ли маршаллинг из свойства messageDTO.needsMarshalling
     * messageDTO != null && messageDTO.getResponse() != null && queue != null
     * @param messageDTO - содержание сообщения
     * @param queue - куда будет отправлено сообщение
     */
    @Override
    public void sendMessage(final SendingMessageDTO messageDTO,final String queue) {
        if(messageDTO != null && messageDTO.getResponse() != null && !StringUtils.isEmpty(queue)){
            try {
                Queue qqueue = selectQueue(queue);
                switch (messageDTO.getType()){
                    case XML:
                        jmsTemplate.send(qqueue, new EFSMessageCreator(
                                !messageDTO.isXmlString() ? jaxbManager.marshall(messageDTO.getResponse()):messageDTO.getResponse().toString(),
                                messageDTO.getProperties()));
                        break;
                    case TEXT:
                        jmsTemplate.send(qqueue, new EFSMessageCreator(
                                messageDTO.getResponse().toString(),
                                messageDTO.getProperties()));
                        break;
                    default:
                        log.error("JMS message type undefined");
                }
            } catch (JAXBException | ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * составляет проперти для jms сообщения
     */
    public static class EFSMessageCreator implements MessageCreator {

        final String message;

        final Map<String, Object> properties;

        public String getMessage() {
            return message;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public EFSMessageCreator(String message, Map<String, Object> properties) {
            this.message = message;
            this.properties = properties;
        }

        public Message createMessage(Session session) throws JMSException {
            Message tMessage = session.createTextMessage(message);
            if (properties != null) {
                for (Map.Entry<String, Object> prop : properties.entrySet()) {
                    if(prop.getValue() != null && !CUSTOM_HEADERS.contains(prop.getKey())){
                        tMessage.setStringProperty(prop.getKey(), prop.getValue().toString());
                    }
                }
            }
            return tMessage;
        }
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public JaxbManager getJaxbManager() {
        return jaxbManager;
    }

    public void setJaxbManager(JaxbManager jaxbManager) {
        this.jaxbManager = jaxbManager;
    }
}
