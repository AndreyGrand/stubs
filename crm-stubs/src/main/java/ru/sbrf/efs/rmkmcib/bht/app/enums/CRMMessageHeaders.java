package ru.sbrf.efs.rmkmcib.bht.app.enums;

/**
 * Created by sbt-manayev-iye on 11.08.2016.
 *
 * хидеры JMS сообщений, задействованные во взаимодействии ERMKM-КСШ
 */
public enum CRMMessageHeaders {
    /**
     * хидеры КСШ-ERMKM
     */
    SERVICE_NAME_HEADER("ServiceName"),
    OPERATION_NAME_HEADER("OperationName"),
    RQ_TM_HEADER("RqTm"),
    SERVICE_NAMESPACE_HEADER("ServiceNamespace"),
    SC_NAME_HEADER("SCName"),
    RQ_UID_HEADER("RqUID"),
    /**
     * хидеры, используемые в приложении, должны быть проигнорированы при отправке ответа, имеют смысл только в самом приложении
     */
    CRM_DB_ACTION_MISS("CRM_DB_ACTION_MISS");

    private String name;

    public String getName() {
        return name;
    }

    CRMMessageHeaders(String name) {
        this.name = name;
    }
}
