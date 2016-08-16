package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by sbt-manayev-iye on 10.08.2016.
 *
 * специализированный класс процесс, в зависимости от типа запроса, а именно(message.getReqClass().getName().concat(message.getResClass().getName()))
 * выбирает нужный процесс для обработки который содержится в getAvailableProcesses(),
 *
 * Как заполняется getAvailableProcesses()
 *
 * После загрузки контекста spring, класс
 * @see ru.sbrf.efs.rmkmcib.bht.app.spring.ProcessRoutingStepApplicationListener
 * для каждого бина, имплементирующего ProcessRoutingStep находит все классы, имплементирующие интерфейс
 * @see ProcessStep
 * и помеченные аннотацией из getProcessSelectorAnnotation()
 * после того как найдет - положит их в getAvailableProcesses()
 */
public interface ProcessRoutingStep extends ProcessStep{
    Map<String, ProcessStep> getAvailableProcesses();
    void setAvailableProcesses(Map<String, ProcessStep> availableProcesses);
    Class<? extends Annotation> getProcessSelectorAnnotation();
}
