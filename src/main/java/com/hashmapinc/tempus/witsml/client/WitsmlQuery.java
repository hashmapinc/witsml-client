package com.hashmapinc.tempus.witsml.client;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class WitsmlQuery implements com.hashmapinc.tempus.witsml.api.WitsmlQuery {

    private boolean bulkData = false;
    private String objectType;
    private final List<String> includedElements = new ArrayList();
    private final List<String> excludedElements = new ArrayList();
    private final List<WitsmlQuery.ElementConstraint> elementConstraints = new ArrayList();
    private final List<WitsmlQuery.AttributeConstraint> attributeConstraints = new ArrayList();

    public WitsmlQuery() {
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isBulkData() {
        return bulkData;
    }

    public void setBulkData(boolean bulkData) {
        this.bulkData = bulkData;
    }

    public void includeElement(String element) {
        if (element == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        } else {
            this.includedElements.add(element);
        }
    }

    public void excludeElement(String element) {
        if (element == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        } else {
            this.excludedElements.add(element);
        }
    }

    public void addElementConstraint(String element, Object value) {
        if (element == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        } else {
            this.elementConstraints.add(new WitsmlQuery.ElementConstraint(element, value));
        }
    }

    public void addAttributeConstraint(String element, String attribute, Object value) {
        if (element == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        } else if (attribute == null) {
            throw new IllegalArgumentException("attributeName cannot be null");
        } else {
            this.attributeConstraints.add(new WitsmlQuery.AttributeConstraint(element, attribute, value));
        }
    }

    private static Element findElement(Element element, String elementName) {
        assert element != null : "root cannot be null";

        assert elementName != null : "name cannot be null";

        Iterator iterator = element.getDescendants(new ElementFilter());

        Element foundElement;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            foundElement = (Element)iterator.next();
        } while(!foundElement.getName().equals(elementName));

        return foundElement;
    }

    private boolean shouldInclude(Element element) {
        assert element != null : "element cannot be null";

        boolean isEmpty = this.includedElements.isEmpty();
        boolean flag = false;

        Element var;
        for(var = element; var != null; var = var.getParentElement()) {
            if (this.includedElements.contains(var.getName())) {
                flag = true;
            }
        }

        boolean flag1 = false;

        for(var = element; var != null; var = var.getParentElement()) {
            if (this.excludedElements.contains(var.getName())) {
                flag1 = true;
            }
        }

        boolean returnVal = !flag1 && (flag || isEmpty);
        return returnVal    ;
    }

    private void applyInclusions(Element element) {
        assert element != null : "root cannot be null";

        HashSet hashSet = new HashSet();
        Iterator iterator = element.getDescendants(new ElementFilter());

        while(iterator.hasNext()) {
            hashSet.add((Element)iterator.next());
        }

        iterator = element.getDescendants(new ElementFilter());

        while(true) {
            Element var;
            do {
                if (!iterator.hasNext()) {
                    iterator = hashSet.iterator();

                    while(iterator.hasNext()) {
                        var = (Element)iterator.next();
                        var.detach();
                    }

                    return;
                }

                var = (Element)iterator.next();
            } while(!this.shouldInclude(var));

            for(Element varRemove = var; varRemove != null; varRemove = varRemove.getParentElement()) {
                hashSet.remove(varRemove);
            }
        }
    }

    private void applyElementConstraints(Element element) {
        assert element != null : "root cannot be null";

        Iterator iterator = this.elementConstraints.iterator();

        while(iterator.hasNext()) {
            WitsmlQuery.ElementConstraint constraint = (WitsmlQuery.ElementConstraint)iterator.next();
            String elementName = constraint.getElementName();
            Element foundElement = findElement(element, elementName);
            if (foundElement != null) {
                String constraintText = constraint.getText();
                if (foundElement.getText().length() == 0) {
                    foundElement.setText(constraintText);
                } else {
                    Element parentElement = foundElement.getParentElement();
                    Element clone = (Element)parentElement.clone();
                    parentElement.getParentElement().addContent(clone);
                    Element var = findElement(clone, elementName);
                    var.setText(constraintText);
                }
            }
        }

    }

    private void applyAttributeConstraints(Element element) {
        assert element != null : "root cannot be null";

        Iterator iterator = this.attributeConstraints.iterator();

        while(iterator.hasNext()) {
            WitsmlQuery.AttributeConstraint attributeConstraint = (WitsmlQuery.AttributeConstraint)iterator.next();
            String elementName = attributeConstraint.getElementName();
            Element foundElement = findElement(element, elementName);
            if (foundElement != null) {
                String attributeName = attributeConstraint.getAttributeName();
                Attribute attribute = foundElement.getAttribute(attributeName);
                if (attribute != null) {
                    attribute.setValue(attributeConstraint.getText());
                }
            }
        }

    }

    public String apply(String xmlString) {
        assert xmlString != null : "queryXml cannot be null";

        SAXBuilder saxBuilder = new SAXBuilder();

        try {
            Document document = saxBuilder.build(new StringReader(xmlString));
            Element rootElement = document.getRootElement();
            this.applyInclusions(rootElement);
            this.applyElementConstraints(rootElement);
            this.applyAttributeConstraints(rootElement);
            String finalXmlString = (new XMLOutputter()).outputString(rootElement.getDocument());
            return finalXmlString;
        } catch (IOException ex) {
            ex.printStackTrace();

            assert false : "Unable to create XML document: " + xmlString;

            return null;
        } catch (JDOMException ex) {
            ex.printStackTrace();

            assert false : "Unable to parse XML document: " + xmlString;

            return null;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Include elements:\n");
        Iterator iterator = this.includedElements.iterator();

        String str;
        while(iterator.hasNext()) {
            str = (String)iterator.next();
            stringBuilder.append("  <" + str + "/>\n");
        }

        stringBuilder.append("Exclude elements:\n");
        iterator = this.excludedElements.iterator();

        while(iterator.hasNext()) {
            str = (String)iterator.next();
            stringBuilder.append("  <" + str + "/>\n");
        }

        stringBuilder.append("Constraints:\n");
        iterator = this.elementConstraints.iterator();

        while(iterator.hasNext()) {
            WitsmlQuery.ElementConstraint elementConstraint = (WitsmlQuery.ElementConstraint)iterator.next();
            stringBuilder.append("  " + elementConstraint + "\n");
        }

        iterator = this.attributeConstraints.iterator();

        while(iterator.hasNext()) {
            WitsmlQuery.AttributeConstraint attributeConstraint = (WitsmlQuery.AttributeConstraint)iterator.next();
            stringBuilder.append("  " + attributeConstraint + "\n");
        }

        return stringBuilder.toString();
    }

    private static class AttributeConstraint extends WitsmlQuery.ElementConstraint {
        private String attributeName;

        AttributeConstraint(String element, String attribute, Object value) {
            super(element, value);

            assert element != null : "elementName cannot be null";

            assert attribute != null : "attributeName cannot be null";

            this.attributeName = attribute;
        }

        String getAttributeName() {
            return this.attributeName;
        }

        public String toString() {
            return "<" + this.elementName + " " + this.attributeName + "=" + this.getText() + "/>";
        }
    }

    private static class ElementConstraint {
        protected final String elementName;
        protected final Object value;

        ElementConstraint(String elementName, Object value) {
            assert elementName != null : "elementName cannot be null";

            this.elementName = elementName;
            this.value = value;
        }

        String getElementName() {
            return this.elementName;
        }

        String getText() {
            if (this.value == null) {
                return "";
            } else {
                return this.value.toString();
            }
        }

        public String toString() {
            return "<" + this.elementName + ">" + this.getText() + "</" + this.elementName + ">";
        }
    }
}
