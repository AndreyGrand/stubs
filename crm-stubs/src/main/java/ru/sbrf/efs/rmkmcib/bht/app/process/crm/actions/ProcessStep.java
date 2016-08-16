package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions;

import org.apache.camel.Exchange;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;

/**
 * Created by sbt-manayev-iye on 03.08.2016.
 *
 * шаг процесса, преобразует данные и возвращает на следующий шаге
 */
public interface ProcessStep {
    /**
     *
     * @param exchange - приходит от Camel, оригинальный запрос
     * @param message - данные для преобразования
     * @return возращает данные после обработки, с данными может произойти все, что угодно, даже смениться ссылка
     */
    CRMMessageVO process(Exchange exchange, CRMMessageVO message) throws CRMMessageProcessingException;

}
