package cwe.fbce.qa.generators;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class DomainDocumentTransformer {

    private Document sourceDocument;
    private NodeList criticalBranchElements;
    private List<String> tsoOriginValues;
    private NamespaceContext namespaceContext = new NamespaceContext() {
        @Override
        public String getNamespaceURI(String prefix) {
            return "flowbased";
        }

        @Override
        public String getPrefix(String namespaceURI) {
            System.out.println("getPrefix - " + namespaceURI);
            return null;
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            System.out.println("getPrefixes - " + namespaceURI);
            return null;
        }
    };

    public void setValidateOutput(boolean validateOutput) {
        this.validateOutput = validateOutput;
    }

    public boolean isValidationPassed() {

        return validationPassed;
    }

    public boolean isValidateOutput() {
        return validateOutput;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    private boolean validationPassed;
    private boolean validateOutput;
    private String validationMessage;


    public void transform(InputStream input, OutputStream output) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        sourceDocument = factory.newDocumentBuilder().parse(input);
        findCriticalBranches();
        System.out.println("CB count: " + criticalBranchElements.getLength());
        findTsoOriginValues();
        processCriticalBranches();
        checkActionSetId();
        checkOutageId();

        validateTransformedDocument();

        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(sourceDocument), new StreamResult(output));

    }

    private void checkOutageId() {
        System.out.println("checking outage/@id");

        String elementName = "outage";
        fixIdAttribute(elementName);
    }

    private void checkActionSetId() {
        System.out.println("checking actionSet/@id");

        String elementName = "actionSet";
        fixIdAttribute(elementName);
    }

    private void fixIdAttribute(String elementName) {
        NodeList nodes = sourceDocument.getElementsByTagNameNS("flowbased", elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            if (element.hasAttribute("id")) {
                continue;
            }
            element.setAttribute("id", UUID.randomUUID().toString());
        }
    }

    private void validateTransformedDocument() throws SAXException, IOException {
        validationMessage = null;
        validationPassed = false;
        if (!validateOutput) {
            return;
        }
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/flowbaseddomaindocument-cgmes-07.xsd"));
        Validator validator = schema.newValidator();

        try {
            validator.validate(new DOMSource(sourceDocument));
            validationPassed = true;
        } catch (SAXException e) {
            validationMessage = e.getMessage();
            System.out.println(validationMessage);
            validationPassed = false;
        }

    }

    private void processCriticalBranches() throws XPathExpressionException {
        System.out.println();
        String separator = "";
        for (int i = 0; i < criticalBranchElements.getLength(); i++) {
            if (i % 100 == 0) {
                System.out.print(separator + "CB " + i);
                System.out.flush();
                separator = ", ";
            }
            Element branchElement = (Element) criticalBranchElements.item(i);
            processCriticalBranch(branchElement);
        }
        System.out.println();
    }

    private void processCriticalBranch(Element branchElement) throws XPathExpressionException {
        Element monitoredBranch = (Element) branchElement.getElementsByTagNameNS("flowbased", "monitoredBranch").item(0);
        if (monitoredBranch == null) {
            return;
        }
        if (!monitoredBranch.hasAttribute("id")) {
            monitoredBranch.setAttribute("id", UUID.randomUUID().toString());
        }
        String tsoOrigin = getTsoOrigin(branchElement);
        String specifiedFrom = getSpecifiedFrom(branchElement);
        String from = tsoOrigin;
        String to = tsoOrigin;
        if (specifiedFrom.toLowerCase().startsWith("x")) {
            to = getOtherTsoOrigin(tsoOrigin);
        }
        monitoredBranch.setAttribute("fromTSO", from);
        monitoredBranch.setAttribute("toTSO", to);


        NodeList childNodes = monitoredBranch.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.TEXT_NODE) {
                monitoredBranch.removeChild(node);
            }
        }

    }


    private Iterator<String> tsoOriginIterator;

    private String getOtherTsoOrigin(String tsoOrigin) {


        if (tsoOriginValues.size() < 2) {
            return tsoOriginValues.get(0);
        }

        String result = tsoOrigin;
        while (result.equals(tsoOrigin)) {
            result = getTsoOriginIterator().next();
        }


        return result;


    }

    private Iterator<String> getTsoOriginIterator() {
        if (tsoOriginIterator == null || !tsoOriginIterator.hasNext()) {
            tsoOriginIterator = tsoOriginValues.iterator();
        }
        return tsoOriginIterator;
    }

    private String getTsoOrigin(Element branchElement) throws XPathExpressionException {
        XPathExpression xPathExpression = getxPathExpression("fb:tsoOrigin");
        return (String) xPathExpression.evaluate(branchElement, XPathConstants.STRING);
    }

    private XPath getxPath() {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(namespaceContext);
        return xPath;
    }

    private String getSpecifiedFrom(Element branchElement) throws XPathExpressionException {
        XPathExpression xPathExpression = getxPathExpression("fb:monitoredBranch/fb:specified/fb:from/@name");
        return (String) xPathExpression.evaluate(branchElement, XPathConstants.STRING);
    }

    private XPathExpression getxPathExpression(String expression) throws XPathExpressionException {
        XPath xPath = getxPath();
        return xPath.compile(expression);
    }

    private void findTsoOriginValues() throws XPathExpressionException {
        Collection<String> origins = new HashSet<>();
        XPathExpression xPathExpression = getxPathExpression("//fb:criticalBranch/fb:tsoOrigin");

        NodeList tsoOrigins = (NodeList) xPathExpression.evaluate(sourceDocument, XPathConstants.NODESET);
        for (int i = 0; i < tsoOrigins.getLength(); i++) {
            String tsoOrigin = ((Element) tsoOrigins.item(i)).getTextContent().trim();
            origins.add(tsoOrigin);
        }

        tsoOriginValues = new ArrayList<>(origins);
        if (origins.size() == 1) {
            return;
        }

        tsoOriginValues.addAll(origins);
        tsoOriginValues.addAll(origins);
        tsoOriginValues.addAll(origins);
        tsoOriginValues.addAll(origins);
        Collections.shuffle(tsoOriginValues);


    }


    private void findCriticalBranches() throws XPathExpressionException {

        XPathExpression xPathExpression = getxPathExpression("//fb:criticalBranch");
        criticalBranchElements = (NodeList) xPathExpression.evaluate(sourceDocument, XPathConstants.NODESET);


    }
}
