package cwe.fbce.qa.generators;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Tests {

    public static Document getInputDocument() throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Tests.class.getResourceAsStream("/fb_final_enlarged.xml"));
    }
}
