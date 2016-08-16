package ru.sbrf.efs.rmkmcib.bht.app.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sbrf.efs.rmkmcib.bht.app.enums.CRMMessageHeaders;
import ru.sbrf.efs.rmkmcib.bht.app.jms.sender.RmKmBHTJmsSender;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessRoutingStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter.MessagesConverter;

/**
 * Created by manaev on 8/2/16.
 *
 */

/**
 * Camel маршрут для обрабоки запросов от CRM
 */
@Service
public class CRMMockRoute extends RouteBuilder {

    @Autowired
    @Qualifier("CRMMessageConverter")
    MessagesConverter converter;

    @Autowired
    @Qualifier("GetResponseFileStep")
    ProcessStep getFileStep;

    @Autowired
    @Qualifier("CRMDatabaseActionProcessStep")
    ProcessRoutingStep getFromDbStep;

    @Autowired
    RmKmBHTJmsSender jmsSender;

    @Value("${activemq.queues.out}")
    String activeMqOutQueue;

    @Value("${activemq.queues.in}")
    String activeMqInQueue;

    /**
     * TODO добавить обработчик ошибок http://camel.apache.org/error-handling-in-camel.html
     */

    /**
     * from("activemq:".concat(activeMqOutQueue)) - получаем сообщение из очереди
     * .bean(converter) - находим классы для преобразовании, создаем CRMMessageVO
     * .bean(getFromDbStep) - достаем xml с ответом из базы и пишем в поле response CRMMessageVO и возвращаем - если не нашли, то пишется
     *  хидер CRM_DB_ACTION_MISS как true
     * .choice()
     *  - если CRM_DB_ACTION_MISS == false jmsSender отправит ответ
     *  - если CRM_DB_ACTION_MISS == true, getFileStep достанет файл заглушки и jmsSender отправит ответ-заглушку
     * @throws Exception
     */
    public void configure() throws Exception {
        from("activemq:".concat(activeMqInQueue)).bean(converter).bean(getFromDbStep).choice()
                .when(header(CRMMessageHeaders.CRM_DB_ACTION_MISS.getName()).isEqualTo(true))
                    .bean(getFileStep).bean(jmsSender, "sendMessage(*,".concat(activeMqOutQueue).concat(")"))
                .when(header(CRMMessageHeaders.CRM_DB_ACTION_MISS.getName()).isEqualTo(false))
                    .bean(jmsSender, "sendMessage(*,".concat(activeMqOutQueue).concat(")"))
                .end();
    }
}
