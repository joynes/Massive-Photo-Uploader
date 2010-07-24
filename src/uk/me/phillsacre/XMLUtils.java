package uk.me.phillsacre;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Utilities class for handling XML data
 * 
 * @author phill
 * 
 */
public class XMLUtils
{
	/**
	 * Retrieves the value of a child node given the name of the child node.
	 * 
	 * @param node
	 * @param childNodeName
	 * @return
	 */
	public static String getChildValue(Node node, String childNodeName)
	{
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node n = children.item(i);
			if (n.getNodeName().equals(childNodeName))
			{
				return getNodeValue(n);
			}
		}

		return null;
	}

	/**
	 * Retrieves a Map of the value of child nodes in the form { [Node Name] =>
	 * [Node Value] }
	 * 
	 * @param node
	 */
	public static Map<String, String> getChildValueMap(Node node)
	{
		HashMap<String, String> map = new HashMap<String, String>();

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node n = children.item(i);
			String nodeName = n.getNodeName();
			String nodeValue = getNodeValue(n);

			map.put(nodeName, nodeValue);
		}

		return map;
	}

	/**
	 * Retrieves the value of a node (i.e, its text content)
	 * 
	 * @param node
	 * @return
	 */
	public static String getNodeValue(Node node)
	{
		return node.getTextContent();
	}

	/**
	 * Creates a DOM document from an XML string
	 * 
	 * @param xml
	 * @return
	 */
	public static Document getDocument(InputStream input)
	{
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			return builder.parse(input);
		}
		catch (Exception e)
		{
			throw new UploaderException("Could not create document", e);
		}
	}
}
