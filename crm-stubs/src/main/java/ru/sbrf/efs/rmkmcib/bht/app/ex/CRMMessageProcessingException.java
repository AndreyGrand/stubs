package ru.sbrf.efs.rmkmcib.bht.app.ex;

/**
 * Created by sbt-manayev-iye on 03.08.2016.
 *
 */

/**
 * легкий(без стектрейса) Exception сигнализирующий о внутренних беспорядках
 */
public class CRMMessageProcessingException extends Exception {
    public CRMMessageProcessingException(String message) {
        super(message, null, false, false);
    }
}
