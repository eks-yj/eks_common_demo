package com.eks.utils;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class DocumentUtils {
    public static Document convertByXmlFilePath(String xmlFilePathString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new File(xmlFilePathString));
    }
    public static Document convertByXmlString(String xmlString) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new InputSource(new StringReader(xmlString)));
    }
    public static String convertDocumentToString(Document document) throws TransformerException {
        return convertDocumentToString(document,false);
    }
    public static String convertDocumentToString(Document document, Boolean indentBoolean) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        if (indentBoolean) {
            //Whether to automatically add extra white space
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //Set the indentation
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        }
        //Whether to ignore XML declarations, omit-xml-declaration 指定了 XSLT 处理器是否应输出 XML 声明，其值必须为 yes 或 no
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource domSource = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        transformer.transform(domSource, streamResult);
        return stringWriter.getBuffer().toString();
    }
    public static String convertNodeToString(Node node){
        return convertNodeToString(node,new StringBuilder());
    }
    private static String convertNodeToString(Node node, StringBuilder stringBuilder) {
        if (node != null) {
            int nodeTypeInt = node.getNodeType();
            switch (nodeTypeInt) {
                case Node.DOCUMENT_NODE: {
                    convertNodeToString(((Document) node).getDocumentElement(), stringBuilder);
                    break;
                }
                case Node.ELEMENT_NODE: {
                    stringBuilder.append("<").append(node.getNodeName());
                    if (node.hasAttributes()) {
                        NamedNodeMap attributesNamedNodeMap = node.getAttributes();
                        for (int i = 0, lenth = attributesNamedNodeMap.getLength(); i < lenth; i++) {
                            convertNodeToString(attributesNamedNodeMap.item(i), stringBuilder);
                        }
                    }
                    stringBuilder.append(">");
                    if (node.hasChildNodes()) {
                        NodeList childNodesNodeList = node.getChildNodes();
                        for (int i = 0; i < childNodesNodeList.getLength(); i++) {
                            convertNodeToString(childNodesNodeList.item(i), stringBuilder);
                        }
                    }
                    break;
                }
                case Node.ATTRIBUTE_NODE: {
                    stringBuilder.append(" ").append(node.getNodeName()).append("=\"");
                    if (node.hasChildNodes()) {
                        NodeList childNodesNodeList = node.getChildNodes();
                        for (int i = 0; i < childNodesNodeList.getLength(); i++) {
                            convertNodeToString(childNodesNodeList.item(i), stringBuilder);
                        }
                    }
                    stringBuilder.append("\"");
                    break;
                }
                case Node.ENTITY_REFERENCE_NODE: {
                    stringBuilder.append("&").append(node.getNodeName()).append(";");
                    break;
                }
                case Node.CDATA_SECTION_NODE: {
                    stringBuilder.append("<![CDATA[").append(node.getNodeValue()).append("]]>");
                    break;
                }
                case Node.TEXT_NODE: {
                    stringBuilder.append(node.getNodeValue());
                    break;
                }
                case Node.COMMENT_NODE: {
                    stringBuilder.append("<!--").append(node.getNodeValue()).append("-->");
                    break;
                }
                //处理指示（processing instruction）
                case Node.PROCESSING_INSTRUCTION_NODE: {
                    stringBuilder.append("<?").append(node.getNodeName()).append(" ").append(node.getNodeValue()).append("?>");
                    break;
                }
                default:
                    return stringBuilder.toString();
            }
            if (nodeTypeInt == Node.ELEMENT_NODE) {
                stringBuilder.append("</").append(node.getNodeName()).append(">");
            }
            return stringBuilder.toString();
        }
        return null;
    }
    public static Document getDocumentFromNode(Node node){
        if (node instanceof Document){
            return (Document)node;
        }else if (node instanceof Element){
            Element element = (Element)node;
            return element.getOwnerDocument();
        }else{
            return node.getOwnerDocument();
        }
    }
}
