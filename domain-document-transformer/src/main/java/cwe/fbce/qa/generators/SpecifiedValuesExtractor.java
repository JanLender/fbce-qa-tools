package cwe.fbce.qa.generators;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

import javax.management.Attribute;
import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;

public class SpecifiedValuesExtractor {

    public List<String> getSpecifiedValues(Document document) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile("//criticalBranch/monitoredBranch/specified/from/@name");
        NodeList specifieds = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < specifieds.getLength(); i++) {
           Attr nameAttribute = (Attr) specifieds.item(i);
            result.add(nameAttribute.getValue());
        }
        return result;
    }
}
