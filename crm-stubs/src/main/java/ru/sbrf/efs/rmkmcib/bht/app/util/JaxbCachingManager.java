package ru.sbrf.efs.rmkmcib.bht.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.sbrf.efs.rmkmcib.bht.app.ex.CRMMessageProcessingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by manaev on 8/2/16.
 */
@Service
public class JaxbCachingManager implements JaxbManager {

    private static final Logger log = LoggerFactory.getLogger(JaxbCachingManager.class);

    ConcurrentHashMap<String, JAXBContext> cache = new ConcurrentHashMap<>();
    private final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    private final TransformerFactory tf = TransformerFactory.newInstance();

    public void setTagTextValue(Document doc, String tag, String value) {
        NodeList nl = doc.getElementsByTagName(tag);
        nl.item(0).setTextContent(value);
    }

    public String docToString(Document doc) throws CRMMessageProcessingException {
        StringWriter writer = new StringWriter();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString().replaceAll("\n|\r", "");
        } catch (TransformerException e) {
            log.error(e.getMessage(), e);
            throw new CRMMessageProcessingException(e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    /**
     * получение документа из xml-строки
     *
     * @param xml
     * @return
     * @throws CRMMessageProcessingException
     */
    public Document getDocFromXmlString(String xml) throws CRMMessageProcessingException {
        try {
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            return docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e.getMessage(), e);
            throw new CRMMessageProcessingException(e.getMessage());
        }
    }

    /**
     * получение имени операции из xml(первый тэг в xml)
     *
     * @param doc имя первого тэга = название операции
     * @return название операции
     */
    public String getOperationName(Document doc) {
        return doc.getFirstChild().getNodeName();
    }

    /**
     * полчение текствого содержания тэгов из doc с именами в tags
     * предполагается, что тээг встречается единожды - иначе будет проигнорирован
     *
     * @param doc  xml документ
     * @param tags тэги для поиска в doc
     * @return текствое содержание тэга
     */
    public Map<String, String> getFirstMatchingTagTextContent(Document doc, String[] tags) {
        Map<String, String> props = new HashMap<>();
        if (tags != null) {
            for (String tag : tags) {
                NodeList nl = doc.getElementsByTagName(tag);
                if (nl != null && nl.getLength() == 1) {
                    props.put(tag, nl.item(0).getTextContent());
                }
            }
        }
        return props;
    }

    public Marshaller getMarshaller(String key) throws JAXBException, ClassNotFoundException {
        JAXBContext temp = cache.get(key);
        if (temp != null) {
            return temp.createMarshaller();
        } else {
            temp = JAXBContext.newInstance(Class.forName(key));
            temp = cache.putIfAbsent(key, temp);
            if (temp == null) {
                temp = cache.get(key);
            }
            return temp.createMarshaller();
        }
    }

    public Unmarshaller getUnmarshaller(String key) throws JAXBException, ClassNotFoundException {
        JAXBContext temp = cache.get(key);
        if (temp != null) {
            return temp.createUnmarshaller();
        } else {
            temp = JAXBContext.newInstance(Class.forName(key));
            temp = cache.putIfAbsent(key, temp);
            if (temp == null) {
                temp = cache.get(key);
            }
            return temp.createUnmarshaller();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unmarshall(String payload, Class<T> clazz) throws JAXBException, ClassNotFoundException {
        Unmarshaller unmarshaller = getUnmarshaller(clazz.getCanonicalName());
        StringReader reader = new StringReader(payload);
        try {
            return (T) unmarshaller.unmarshal(reader);
        } finally {
            reader.close();

        }
    }

    public String marshall(Object payload) throws JAXBException, ClassNotFoundException {
        StringWriter sw = new StringWriter();
        try {
            Marshaller mm = getMarshaller(payload.getClass().getCanonicalName());
            mm.marshal(payload, sw);
            return sw.toString();
        } finally {
            try {
                sw.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
