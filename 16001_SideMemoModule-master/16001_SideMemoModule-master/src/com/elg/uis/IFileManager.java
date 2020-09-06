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
     * XML 파일을 읽어 파싱하여 key(Tree Node key)를 리턴한다. 
     * @param filePath XML FILE PATH
     * @param fileName XML FILE NAME
	 * @return key TreeNode id
     * @throws Exception
     */
	public int readKeyFile(String filePath, String fileName) throws Exception;
	
	/**
     * XML 파일을 읽어 파싱하여 DefaultMutableTreeNode(Tree Root Node)를 리턴한다. 
     * @param filePath XML FILE PATH
     * @param fileName XML FILE NAME
     * @return DefaultMutableTreeNode(Tree Root Node)
     * @throws Exception
     */
	public DefaultMutableTreeNode read(String filePath, String fileName) throws Exception; 
	
	/**
	 * TreeModel의 데이터를 XML 파일에 write한다.
	 * @param filePath XML FILE PATH
	 * @param fileName XML FILE NAME
	 * @param treeModel Tree모델
	 */
	public void write(String filePath, String fileName, TreeModel treeModel);
	
	/**
	 * Tree Node key를 XML 파일에 write한다.
	 * @param filePath XML FILE PATH
	 * @param fileName XML FILE NAME
	 * @param key Tree Node id
	 */
	public void writeKeyFile(String filePath, String fileName, int key);
}
