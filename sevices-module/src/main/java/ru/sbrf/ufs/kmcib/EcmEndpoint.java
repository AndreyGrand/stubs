package ru.sbrf.ufs.kmcib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import ru.sbrf.ufs.ecm.api.Document;
import ru.sbrf.ufs.ecm.api.ObjectFactory;
import ru.sbrf.ufs.ecm.api.Objects;
import ru.sbrf.ufs.ecm.api.TraverseTree;

@Endpoint
public class EcmEndpoint {
	//public static final String NAMESPACE_URI = "http://ecm.sbrf.ru/port";
	private Logger log = LoggerFactory.getLogger(EcmEndpoint.class);

		@PayloadRoot(localPart = "traverseTree")
		@ResponsePayload
		public JAXBElement<Objects> traverseTree(@RequestPayload TraverseTree traverseTree) {
			log.info("traverseTree method call");
			log.info("traverseTree target: " + traverseTree.getTarget());
			Objects value = new Objects();
			List<Document> listFoldersOrDocuments = value.getDocumentOrFolder();
			Document book = new Document();
			book.setName("book");
			Document book1 = new Document();
			book1.setName("another book");
			book1.setClazz("className");
			listFoldersOrDocuments.add(book);
			listFoldersOrDocuments.add(book1);
			
			return new ObjectFactory().createObjects(value);
		}
}
