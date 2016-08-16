package ru.sbrf.efs.rmkmcib.bht.app.process.crm.converter;

import org.apache.camel.Exchange;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.vo.CRMMessageVO;

/**
 * Created by sbt-manayev-iye on 03.08.2016.
 *
 */

/**
 * преобразует оригинальный запрос от Camel во внутренний  CRMMessageVO для дальнейшей обработки
 * определяет, какие классы нужны для запросов-ответов
 */
public interface MessagesConverter {
     CRMMessageVO process(Exchange exchange) throws Exception;

}
