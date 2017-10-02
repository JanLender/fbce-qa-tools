package cwe.fbce.qa.generators;

import org.junit.Test;
import static org.junit.Assert.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DomainDocumentValidation {


    @Test
    public void readDocument() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Document document = Tests.getInputDocument();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile("//criticalBranch/monitoredBranch/specified/from/@name");

        NodeList specifieds = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        assertTrue(specifieds.getLength() > 0);



    }

    @Test
    public void extractTsoOriginValues() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Document document = Tests.getInputDocument();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile("//criticalBranch/tsoOrigin");

        NodeList tsoOrigins = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < tsoOrigins.getLength(); i++) {
            String tsoOrigin = ((Element) tsoOrigins.item(i)).getTextContent().trim();
            System.out.println(tsoOrigin);
        }





    }



    @Test
    public void validateDocument() throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/flowbaseddomaindocument-07.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new File("src/test/resources/fb_final_minimal.xml")));
    }

    @Test
    public void validateDOMDocument() throws SAXException, IOException, ParserConfigurationException, TransformerException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/flowbaseddomaindocument-07.xsd"));
        Validator validator = schema.newValidator();
        InputStream inputStream = this.getClass().getResourceAsStream("/fb_final_minimal.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document sourceDocument = factory.newDocumentBuilder().parse(inputStream);
        System.out.println(sourceDocument.getNamespaceURI());
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(sourceDocument), new StreamResult(System.out));
        validator.validate(new DOMSource(sourceDocument));
    }
}
