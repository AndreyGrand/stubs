package ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter;

/**
 * Created by manaev on 8/2/16.
 *
 */

import java.util.Map;

/**
 * для КСШ, как я понял, хидеры JMS ServiceName и OperationName являются ключом для маршрутизации
 * по ним выбирается способ обработки
 * CRMClassBounds.class отвечает, в какие xml-классы преобразуются строки из запроса-ответа
 */
public interface CRMClassBounds {
    CRMClassBoundsImpl.ReqResClass getBoundClasses(String serviceName,String operationName);
    Map<String, CRMClassBoundsImpl.ReqResClass> getKnownClasses();
    String getBoundServiceName(String operationName);
    CRMClassBoundsImpl.ReqResClass getBoundClasses(String operationName);
}
