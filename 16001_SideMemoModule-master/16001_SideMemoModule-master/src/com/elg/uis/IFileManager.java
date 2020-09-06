package com.elg.uis;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
* <pre>
* <p> Title: IFileManager.java </p>
* <p> Description: XML FILE read/write Interface</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public interface IFileManager {
	
	/**
     * XML ������ �о� �Ľ��Ͽ� key(Tree Node key)�� �����Ѵ�. 
     * @param filePath XML FILE PATH
     * @param fileName XML FILE NAME
	 * @return key TreeNode id
     * @throws Exception
     */
	public int readKeyFile(String filePath, String fileName) throws Exception;
	
	/**
     * XML ������ �о� �Ľ��Ͽ� DefaultMutableTreeNode(Tree Root Node)�� �����Ѵ�. 
     * @param filePath XML FILE PATH
     * @param fileName XML FILE NAME
     * @return DefaultMutableTreeNode(Tree Root Node)
     * @throws Exception
     */
	public DefaultMutableTreeNode read(String filePath, String fileName) throws Exception; 
	
	/**
	 * TreeModel�� �����͸� XML ���Ͽ� write�Ѵ�.
	 * @param filePath XML FILE PATH
	 * @param fileName XML FILE NAME
	 * @param treeModel Tree��
	 */
	public void write(String filePath, String fileName, TreeModel treeModel);
	
	/**
	 * Tree Node key�� XML ���Ͽ� write�Ѵ�.
	 * @param filePath XML FILE PATH
	 * @param fileName XML FILE NAME
	 * @param key Tree Node id
	 */
	public void writeKeyFile(String filePath, String fileName, int key);
}
