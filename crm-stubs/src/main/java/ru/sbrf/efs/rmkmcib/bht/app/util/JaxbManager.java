package ru.sbrf.efs.rmkmcib.bht.app.util;

import org.w3c.dom.Document;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.Map;

/**
 * Created by manaev on 8/2/16.
 *
 */

/**
 * утилита для работы с xml
 * маршалит - анмаршилат, создает JAXBContext и кеширует их
 */
public interface JaxbManager {
    Marshaller getMarshaller(String key) throws JAXBException, ClassNotFoundException;

    String marshall(Object payload) throws JAXBException, ClassNotFoundException;

    public <T> T unmarshall(String payload, Class<T> clazz) throws JAXBException, ClassNotFoundException;

    Document getDocFromXmlString(String xml) throws CRMMessageProcessingException;

    Map<String, String> getFirstMatchingTagTextContent(Document doc, String[] tags);

    String getOperationName(Document doc);

    String docToString(Document doc) throws CRMMessageProcessingException;

    void setTagTextValue(Document doc, String tag, String value);

}
