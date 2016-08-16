package ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.db;

import java.lang.annotation.*;

/**
 * Created by sbt-manayev-iye on 10.08.2016.
 *
 * аннотация для CRM сервисов, указывается requestClass - класс запроса сервиса(например GetlegalClientManagerTaskListRqType.class)
 * responseClass - класс ответа сервиса(например GetlegalClientManagerTaskListRsType.class)
 *
 * аннотация показывает, что класс ею помеченный, является одним из классов-процессов
 * @see ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessStep
 * для обработки
 *
 * такие аннотации являются выходными параметрами в
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CRMBoundedClassesProcessor {
    Class requestClass();
    Class responseClass();
}
