package ru.sbrf.efs.rmkmcib.bht.app.jms.sender;

/**
 * Created by sbt-manayev-iye on 02.08.2016.
 *
 */
public interface RmKmBHTJmsSender {
    public void sendMessage(final SendingMessageDTO messageDTO,String queue);
}
