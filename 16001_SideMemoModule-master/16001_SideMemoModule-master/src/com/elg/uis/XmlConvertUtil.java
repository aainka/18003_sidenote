package com.elg.uis;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
* <pre>
* <p> Title: XmlConvertUtil.java </p>
* <p> Description: XML File read/wirte�ϴ� Util Ŭ����</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class XmlConvertUtil  {
	
	//XML Element : <ROOT> (Tree Root Node ����)
	private final String ELEMENT_ROOT = "ROOT";
	//XML Element : <NODE> (Tree Node ����)
	private final String ELEMENT_NODE = "NODE";
	
	//XML Attribute : NAME (Tree Node ��) 
	private final String ELEMENT_ATTRIBUTE_NAME = "NAME";
	//XML Attribute : DESC (Tree Node ����)
	private final String ELEMENT_ATTRIBUTE_DESC = "DESC";
	//XML Attribute : BACKGROUND_COLOR (Tree Node Background Color)
	private final String ELEMENT_ATTRIBUTE_BACKGROUND_COLOR = "BACKGROUND_COLOR";
	//XML Attribute : FOREROUND_COLOR (Tree Node Foreground Color)
	private final String ELEMENT_ATTRIBUTE_FOREGROUND_COLOR = "FOREGROUND_COLOR";
	//XML Attribute : X (Tree Node Dialog location x)
	private final String ELEMENT_ATTRIBUTE_X = "X";
	//XML Attribute : Y (Tree Node Dialog location y)
	private final String ELEMENT_ATTRIBUTE_Y = "Y";
	//XML Attribute : WIDTH (Tree Node Dialog width)
	private final String ELEMENT_ATTRIBUTE_WIDTH = "WIDTH";
	//XML Attribute : HEIGHT (Tree Node Dialog height)
	private final String ELEMENT_ATTRIBUTE_HEIGHT = "HEIGHT";
	// XML Attribute : FILE_PATH (refer file path)
	private final String ELEMENT_ATTRIBUTE_REFER_FILE_PATH = "REFER_FILE_PATH";
	
	// XML Attribute : KEY(Node id)
	private final String KEY_SEQ = "KEY";
	
	
	private String filePath = "";
	private String fileName = "";
	
	/**
	 * ������
	 */
	public XmlConvertUtil() { }
	
	/**
	 * XML ������ �о� �Ľ��Ͽ� key(Tree Node id)�� �����Ѵ�.
	 * @param filePath XML FILE PATH 
	 * @param fileName XML FILE NAME
	 * @return key (Tree Node id)
	 * @throws Exception
	 */
	public int readKeyFile(String filePath, String fileName) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);
        
		DocumentBuilder builder;
		
		int key = 0;
		
		System.out.println("Read File");
		try {
			//1. ���丮�� äũ�Ѵ�.
			File file = new File(filePath);
			if(!file.exists())
			{
				file.mkdir();
			}
			
			builder = factory.newDocumentBuilder();
			
		    Document xmlDocument = builder.parse(new File(filePath + File.separator + fileName));
		    DOMSource domSource = new DOMSource(xmlDocument);
		    domSource.getNode();

		    Element element = xmlDocument.getDocumentElement();
			key = Integer.parseInt(element.getAttribute(KEY_SEQ));
		    
		} catch (ParserConfigurationException e) {
			throw e;
			
		} catch (SAXException e) {
			throw e;
			
		} catch (IOException e) {
			throw e;
			
		} catch (Exception e) {
			throw e;
		}
		
		return key;
	}
	
	/**
	 * ���� key���� XML ���Ͽ� write�Ѵ�.
	 * @param filePath XML FILE PATH
	 * @param fileName XML FILE NAME
	 * @param key Tree Node id
	 * @return �������� (true : ����, false : ����)
	 */
	public boolean writeKeyFile(String filePath, String fileName, int key) {
		
		boolean resultFlag = false;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		System.out.println("Write Memo");
		
		try {
			//1. ���丮�� äũ�Ѵ�.
			File file = new File(filePath);
			if(!file.exists())
			{
				file.mkdir();
			}
			
			builder = factory.newDocumentBuilder();
		    DOMImplementation impl = builder.getDOMImplementation();
			
		    // Build an XML document from the tree model
		    Document doc = impl.createDocument(null,null,null);
		    
		    String updateKey = "" + key;
		    
			Element element = null;

			element = doc.createElement(ELEMENT_ROOT);
			element.setAttribute(KEY_SEQ, updateKey);
		    
		    doc.appendChild(element);
		    
		    // Transform the document into a string
		    DOMSource domSource = new DOMSource(doc);

		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    
		    StreamResult streamResult = new StreamResult(new File(filePath + File.separator + fileName));

			transformer.transform(domSource, streamResult);

			resultFlag = true;
		    
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return resultFlag;
	}
	
	/**
	 * XML ������ �о� �Ľ��Ͽ� DefaultMutableTreeNode(Tree Root Node)�� �����Ѵ�.
	 * @param filePath XML FILE PATH 
	 * @param fileName XML FILE NAME
	 * @return DefaultMutableTreeNode(Tree Root Node)
	 * @throws Exception
	 */
	public DefaultMutableTreeNode readFile(String filePath, String fileName) throws Exception{
		this.filePath = filePath;
		this.fileName = fileName;
		
		System.out.println("Read Node ...");
		DefaultMutableTreeNode defaultMutableTreeNode = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);
        
		DocumentBuilder builder;
			
		try {
			builder = factory.newDocumentBuilder();
		    Document xmlDocument = builder.parse(new File(filePath + File.separator + fileName));
		    DOMSource domSource = new DOMSource(xmlDocument);
		    domSource.getNode();

		    defaultMutableTreeNode = convertXmlToTreeNode(xmlDocument.getDocumentElement());
		    
		} catch (ParserConfigurationException e) {
			throw e;
			
		} catch (SAXException e) {
			throw e;
			
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return defaultMutableTreeNode;
	}

	/**
	 * TreeModel�� �����͸� XML ���Ͽ� write�Ѵ�.
	 * @param filePath XML FILE PATH
	 * @param fileName XML FILE NAME
	 * @param treeModel Tree��
	 * @return �������� (true : ����, false : ����)
	 */
	public boolean writeFile(String filePath, String fileName, TreeModel treeModel) {
		
		boolean resultFlag = false;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			//1. ���丮�� äũ�Ѵ�.
			File file = new File(filePath);
			if(!file.exists())
			{
				file.mkdir();
			}
			
			builder = factory.newDocumentBuilder();
		    DOMImplementation impl = builder.getDOMImplementation();
			
		    // Build an XML document from the tree model
		    Document doc = impl.createDocument(null,null,null);
		    
		    Element root = convertTreeNodeToXml(doc, treeModel, treeModel.getRoot());
		    doc.appendChild(root);
		    
		    // Transform the document into a string
		    DOMSource domSource = new DOMSource(doc);

		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    
		    StreamResult streamResult = new StreamResult(new File(filePath + File.separator + fileName));

			transformer.transform(domSource, streamResult);

			resultFlag = true;
		    
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return resultFlag;
	}
	
	/**
	 * TreeMode�� ������(DefaultMutableTreeNode)�� XML�� ��ȯ�Ѵ�. 
	 * @param doc XML Document
	 * @param model TreeModel
	 * @param node TreeModel ������ ��ü (DefaultMutableTreeNode)  
	 * @return XML�� rootElement
	 * XML ����
	 * <ROOT NAME="memo 1">
	 *    <NODE KEY="1" NAME="aaaa" DESC="aaaa ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"/> 
	 *      <NODE KEY="2" NAME="1111" DESC="1111 ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *      <NODE KEY="3" NAME="2222" DESC="2222 ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *      <NODE KEY="4" NAME="3333" DESC="3333 ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *   </NODE>
	 *   <NODE KEY="5" NAME="bbbb" DESC="bbbb����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *      <NODE KEY="6" NAME="1111" DESC="1111 ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *      <NODE KEY="7" NAME="2222" DESC="2222 ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *      <NODE KEY="8" NAME="3333" DESC="3333 ����" BACKGROUND_COLOR="-1184275" FOREGROUND_COLOR="-16777216" X="-1" Y="-1" WIDTH="-1" HEIGHT="-1" REFER_FILE_PATH="C:\\tmp"//>
	 *   </NODE>
	 * </ROOT>
	 *
	 */
	private Element convertTreeNodeToXml(Document doc, TreeModel model, Object node) {
		
		
		DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)node;
		
		TreeNode treeNode = (TreeNode)defaultMutableTreeNode.getUserObject();

		String name = treeNode.getName();
		String desc = treeNode.getDesc();
		String key = treeNode.getKey() + "";
		
		String referFilePath = treeNode.getFilePath();
		
		int backgroundColor = treeNode.getBackgroundColor();
		int foregroundColor = treeNode.getForegroundColor();
	
		Element element = null;
		if(node.equals(model.getRoot()))
		{	
			element = doc.createElement(ELEMENT_ROOT);
			element.setAttribute(ELEMENT_ATTRIBUTE_NAME, name);
		}
		else
		{
			int x = treeNode.getX();
			int y = treeNode.getY();
			
			int width = treeNode.getWidth();
			int height = treeNode.getHeight();
			
			element = doc.createElement(ELEMENT_NODE);
			element.setAttribute(KEY_SEQ, key);
			element.setAttribute(ELEMENT_ATTRIBUTE_DESC, desc);
			element.setAttribute(ELEMENT_ATTRIBUTE_NAME, name);
			element.setAttribute(ELEMENT_ATTRIBUTE_BACKGROUND_COLOR, String.valueOf(backgroundColor));
			element.setAttribute(ELEMENT_ATTRIBUTE_FOREGROUND_COLOR, String.valueOf(foregroundColor));
			
			element.setAttribute(ELEMENT_ATTRIBUTE_X, String.valueOf(x));
			element.setAttribute(ELEMENT_ATTRIBUTE_Y, String.valueOf(y));
			element.setAttribute(ELEMENT_ATTRIBUTE_WIDTH, String.valueOf(width));
			element.setAttribute(ELEMENT_ATTRIBUTE_HEIGHT, String.valueOf(height));
			
			element.setAttribute(ELEMENT_ATTRIBUTE_REFER_FILE_PATH, referFilePath);
		}
		
	    for(int i=0;i<model.getChildCount(node);i++){
	        Object child = model.getChild(node, i);
	        element.appendChild(convertTreeNodeToXml(doc,model,child));
	    }
	    
	    return element;
	}

	/**
	 * XML�� Element�� DefaultMutableTreeNode�� ��ȯ�Ѵ�.
	 * @param element XML Root Element
	 * @return DefaultMutableTreeNode (TREE ROOT MODEL)
	 */
	private DefaultMutableTreeNode convertXmlToTreeNode(Element element) {
		NodeList nodeList = element.getChildNodes();
		
		boolean keyCheckFlag = false;
		
		int key = 0;
		
		try {
			String keyStr = element.getAttribute(KEY_SEQ);
			String rootStr = element.getNodeName();
			
			if(ELEMENT_ROOT != rootStr) {
				if("0".equals(keyStr) || "".equals(keyStr)) {
					key = FileManager.getInstance().getKey();
					keyCheckFlag = true;
					
				} else {
					key = Integer.parseInt(keyStr);
				}
			}
		}
		catch (Exception e)
		{
			key = FileManager.getInstance().getKey();
			e.printStackTrace();
		} 
		
		String name = element.getAttribute(ELEMENT_ATTRIBUTE_NAME);
		
		String desc = element.getAttribute(ELEMENT_ATTRIBUTE_DESC);
		if(desc == null)
		{
			desc = "";
		}

		int backgroundColor = 0;
		try
		{
			String backgroundColorStr = element.getAttribute(ELEMENT_ATTRIBUTE_BACKGROUND_COLOR);
			if("".equals(backgroundColorStr) || backgroundColorStr==null) {
				backgroundColor = TreeNode.DEFAULT_BACKGROUND_COLOR;
				
			} else {
				backgroundColor = Integer.parseInt(backgroundColorStr);
			}
		}
		catch(Exception e)
		{
			backgroundColor = TreeNode.DEFAULT_BACKGROUND_COLOR;
		}
		
		
		int foregroundColor = 0;
		try
		{
			String foregroundColorStr = element.getAttribute(ELEMENT_ATTRIBUTE_FOREGROUND_COLOR);
			if("".equals(foregroundColorStr) || foregroundColorStr==null) {
				foregroundColor = TreeNode.DEFAULT_FOREGROUND_COLOR;
				
			} else {
				foregroundColor = Integer.parseInt(foregroundColorStr);
			}
		}
		catch(Exception e)
		{
			backgroundColor = TreeNode.DEFAULT_FOREGROUND_COLOR;
		}
		
		String referFilePath = "";
		try
		{
			String referFilePathStr = element.getAttribute(ELEMENT_ATTRIBUTE_REFER_FILE_PATH);
			if("".equals(referFilePathStr) || referFilePathStr==null) {
				referFilePath = TreeNode.DEFAULT_REFER_FILE_PATH;
				
			} else {
				referFilePath = referFilePathStr;
			}
		}
		catch(Exception e)
		{
			referFilePath = TreeNode.DEFAULT_REFER_FILE_PATH;
		}
		
		TreeNode treeNode = new TreeNode(key, name, desc, backgroundColor, foregroundColor, referFilePath);
			
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		try
		{
			String xStr = element.getAttribute(ELEMENT_ATTRIBUTE_X);
			String yStr = element.getAttribute(ELEMENT_ATTRIBUTE_Y);
			String widthStr = element.getAttribute(ELEMENT_ATTRIBUTE_WIDTH);
			String heightStr = element.getAttribute(ELEMENT_ATTRIBUTE_HEIGHT);
			
			if(!("".equals(xStr) || xStr==null)) {
				x = Integer.parseInt(xStr);
			} else {
				x = TreeNode.DEFAULT_X;
			}
			
			if(!("".equals(yStr) || yStr==null)) {
				y = Integer.parseInt(yStr);
			} else {
				y = TreeNode.DEFAULT_Y;
			}
			
			if(!("".equals(widthStr) || widthStr==null)) {
				width = Integer.parseInt(widthStr);
			} else {
				width = TreeNode.DEFAULT_WIDTH;
			}
			
			if(!("".equals(heightStr) || heightStr==null)) {
				height = Integer.parseInt(heightStr);
			} else {
				height = TreeNode.DEFAULT_HEIGHT;
			}
		}
		catch(Exception e)
		{
			x = TreeNode.DEFAULT_X;
			y = TreeNode.DEFAULT_Y;
			width = TreeNode.DEFAULT_WIDTH;
			height = TreeNode.DEFAULT_HEIGHT;
		}
		
		treeNode.setX(x);
		treeNode.setY(y);
		treeNode.setWidth(width);
		treeNode.setHeight(height);
		
		
		DefaultMutableTreeNode result = new DefaultMutableTreeNode(treeNode);
		
		for(int i=0; i<nodeList.getLength(); i++) {
			if(i > 0 && nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				result.add(convertXmlToTreeNode((Element)nodeList.item(i).getChildNodes()));
			}
		}

		if(keyCheckFlag) {
			FileManager.getInstance().write(filePath, fileName, new  DefaultTreeModel(result));
		}
		
   	   return result;
	}
}