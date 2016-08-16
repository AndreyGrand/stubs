package ru.sbrf.efs.rmkmcib.bht.app.jms.sender;

import java.util.Map;

/**
 * Created by sbt-manayev-iye on 03.08.2016.
 *
 */
public interface SendingMessageDTO {
    /**
     *
     * @return проперти Jms сообщения для отправки
     */
    Map<String,Object> getProperties();

    /**
     *
     * @return сообщение для отправки
     */
    Object getResponse();

    /**
     *
     * @return тип сообщения XML, TEXT, в зависимоти от него перед отправкой преобразуется payload
     */
    RmKmJmsMessageType getType();

    /**
     *
     * @return нужено ли маршаллить в строку payload в случае type=XML, или это уже XML строка
     */
    boolean isXmlString();
}
