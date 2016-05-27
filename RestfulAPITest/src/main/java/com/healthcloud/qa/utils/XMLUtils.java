package com.healthcloud.qa.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtils {

	public HashMap getXMLNodeValues(String xmlFile, String required) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("file:///"+xmlFile);

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			return null;
		}catch(SAXException se) {
			se.printStackTrace();
			return null;
		}catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		}

		//get the root elememt
		Element rootElement = dom.getDocumentElement();
		//get a nodelist of required elements
		NodeList nodeList = rootElement.getElementsByTagName(required);

		HashMap map = new HashMap();

		if(nodeList != null && nodeList.getLength() > 0)
			for(int i = 0 ; i < nodeList.getLength();i++) {
				Element el = (Element)nodeList.item(i);
				NodeList nl = el.getElementsByTagName("param");

				if(nl != null && nl.getLength() > 0)
					for(int j = 0 ; j < nl.getLength(); j++) {
						Element elem = (Element)nl.item(j);
						String key = elem.getAttribute("name");
						try {
							String value = elem.getFirstChild().getNodeValue();
							map.put(key, value);
						} catch (Exception e) {
							//returning null if the field does not contain any value in xml file
							map.put(key, null);
						}
					}
			} //end of for loop

		return map;
	}

	public HashMap getInputNodeValues(String xmlFile) {
		return getXMLNodeValues(xmlFile, "input");
	}

	public HashMap getAttributeNodeValues(String xmlFile) {
		return getXMLNodeValues(xmlFile, "attribute");
	}

	public HashMap getValueNodeValues(String xmlFile) {
		return getXMLNodeValues(xmlFile, "value");
	}

	public HashMap getComparatorNodeValues(String xmlFile) {
		return getXMLNodeValues(xmlFile, "comparator");
	}

	public HashMap getConjunctionNodeValues(String xmlFile) {
		return getXMLNodeValues(xmlFile, "conjunction");
	}

	public HashMap getOutputNodeValues(String xmlFile) {
		return getXMLNodeValues(xmlFile, "output");
	}

	public void createFileFromDOM(String filePath, Document dom) throws TransformerException, IOException{
        	TransformerFactory factory = TransformerFactory.newInstance();
	        Transformer transformer = factory.newTransformer();

        	transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        // Create string from xml tree
	        StringWriter sw = new StringWriter();
        	StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(dom);
        	transformer.transform(source, result);
	        String xmlString = sw.toString();

	        File file = new File(filePath);
        	BufferedWriter bw = new BufferedWriter
                	      (new OutputStreamWriter(new FileOutputStream(file)));
	        bw.write(xmlString);
        	bw.flush();
	        bw.close();
	}
	
	public static Node getChildNode(Node parentNode, String nodeName) {
		NodeList nodeList = parentNode.getChildNodes();
		for(int i=1; i<nodeList.getLength(); ++i) {
			Node node = nodeList.item(i);
			if(node.getNodeName().equals(nodeName)) {
				System.out.println("Found child node: <" + nodeName + ">");
				return node;
			}
		}
		return null;
	}
	
	public static Node getChildNodeWithValue(Node parentNode, String nodeName, String nodeValue, boolean ignoreCase) {
		NodeList nodeList = parentNode.getChildNodes();
		for(int i=1; i<nodeList.getLength(); ++i) {
			Node node = nodeList.item(i);
			if(node.getNodeName().equals(nodeName)) {
				if(ignoreCase) {
					if(node.getTextContent().equalsIgnoreCase(nodeValue)) {
						System.out.println("Found child node: <" + nodeName + ">" + nodeValue + "<" + nodeName + "\">");
						return node;
					}
				} else {
					if(node.getTextContent().equals(nodeValue)) {
						System.out.println("Found child node: <" + nodeName + ">" + nodeValue + "<" + nodeName + "\">");
						return node;
					}
				}
			}
		}
		return null;
	}
	
	public static Node getChildNodeWithAttribute(Node parentNode, String nodeName, String attrName, String attrValue, boolean ignoreCase) {
		NodeList nodeList = parentNode.getChildNodes();
		for (int i = 1; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item(i);
			//System.out.println("<" + node.getNodeName() + ">");
			if (node.getNodeName().equals(nodeName)) {
				NamedNodeMap nodeMap = node.getAttributes();
				Node attr = nodeMap.getNamedItem(attrName);
				if (attr != null) {
					//System.out.println("<" + nodeName + " " + attrName + "=\"" + attr.getTextContent() + "\">");
					if (ignoreCase) {
						if (attr.getTextContent().equalsIgnoreCase(attrValue)) {
							System.out.println("Found child node: <" + nodeName + " " + attrName + "=\"" + attrValue + "\" ...>");
							return node;
						}
					} else {
						if (attr.getTextContent().equals(attrValue)) {
							System.out.println("Found child node: <" + nodeName + " " + attrName + "=\"" + attrValue + "\" ...>");
							return node;
						}
					}
				}
			}
		}
		return null;
	}	
	
	
	
	
}
