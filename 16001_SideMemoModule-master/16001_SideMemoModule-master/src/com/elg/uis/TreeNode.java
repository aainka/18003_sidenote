package com.elg.uis;

import java.awt.Color;

/**
 * 
* <pre>
* <p> Title: TreeNode.java </p>
* <p> Description: TreeNode 클래스</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class TreeNode implements Cloneable {

	//DEFAULT Tree Node Background Color
	public static final int DEFAULT_BACKGROUND_COLOR = new Color(237,237,237).getRGB();
	
	//DEFAULT Tree Node Foreground Color
	public static final int DEFAULT_FOREGROUND_COLOR = Color.black.getRGB();
	
	//DEFAULT Tree Node Dialog location x
	public static final int DEFAULT_X = -1;

	//DEFAULT Tree Node Dialog location x
	public static final int DEFAULT_Y = -1;
	
	//DEFAULT Tree Node Dialog location x
	public static final int DEFAULT_WIDTH = -1;
	
	//DEFAULT Tree Node Dialog location x
	public static final int DEFAULT_HEIGHT = -1;
	
	//DEFAULT Tree Node DESC
	public static final String DEFAULT_DESC = "";
	
	//DEFAULT FILE_PATH
	public static final String DEFAULT_REFER_FILE_PATH = "";
	
	//Tree Node 명
	private String name;
	
	//Tree Node 설명
	private String desc;
	
	//Tree Node Background Color
	private int backgroundColor;
	
	//Tree Node Foreground Color
	private int foregroundColor;
	
	//Tree Node Dialog location x
	private int x;
	
	//Tree Node Dialog location y
	private int y;
	
	//Tree Node Dialog width
	private int width;
	
	//Tree Node Dialog height
	private int height;
	
	//Tree Node id
	private int key;
	
	//Tree Node refer file path
	private String filePath;
	
	/**
	 * 최초 생성자
	 * @param name Tree Node 명
	 * @param key Tree Node id 
	 */
	public TreeNode(String name, String desc)
	{
		this.name = name;
		this.desc = DEFAULT_DESC;
		this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
		this.foregroundColor = DEFAULT_FOREGROUND_COLOR;
	}
	
	/**
	 * 생성자
	 * @param name Tree Node 명
	 * @param key Tree Node id 
	 */
	public TreeNode(int key, String name)
	{
		this(key, name, DEFAULT_DESC);
	}
	
	/**
	 * 생성자
	 * @param name Tree Node 명 
	 * @param desc Tree Node 설명
	 */
	public TreeNode(int key, String name, String desc)
	{
		this(key, name, desc, DEFAULT_BACKGROUND_COLOR, DEFAULT_FOREGROUND_COLOR, DEFAULT_REFER_FILE_PATH);
	}
	
	/**
	 * 생성자
	 * @param name Tree Node 명
	 * @param desc Tree Node 설명
	 * @param backgroundColor Tree Node Background Color
	 * @param foregroundColor Tree Node Foreground Color
	 */
	public TreeNode(int key, String name, String desc, int backgroundColor, int foregroundColor, String filePath) {
		this.key = key;
		this.name = name;
		this.desc = desc;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.filePath = filePath;
	}

	/**
	 * Tree Node id를 리턴한다.
	 * @return
	 */
	public int getKey() {
		return key;
	}
	
	/**
	 * Tree Node명을 리턴나다.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Tree Node명을 설정한다.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Tree Node 설명을 리턴나다.
	 * @return
	 */
	public String getDesc() {
		return desc;
	}
	
	/**
	 * Tree Node 설명을 설정한다.
	 * @param desc Tree Node 설명
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * Tree Node Background Color를 리턴한다.
	 * @return Tree Node Background Color
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	
	/**
	 * Tree Node Background Color를 설정한다.
	 * @param backgroundColor Tree Node Background Color
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Tree Node Foreground Color를 리턴한다.
	 * @return Tree Node Foreground Color
	 */
	public int getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * Tree Node Foreground Color를 설정한다.
	 * @param foregroundColor Tree Node Foreground Color
	 */
	public void setForegroundColor(int foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	
	/**
	 * Tree Node Dialog의 출력 X좌표를 리턴한다.
	 * return Tree Node Dialog locationX
	 */
	public int getX() {
		return x;
	}

	/**
	 * Tree Node Dialog의 출력 X좌표를 설정한다.
	 * @param locationX Tree Node Dialog locationX
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Tree Node Dialog의 출력 Y좌표를 리턴한다.
	 * return Tree Node Dialog locationY
	 */
	public int getY() {
		return y;
	}

	/**
	 * Tree Node Dialog의 출력 Y좌표를 설정한다.
	 * @param locationY Tree Node Dialog locationY
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Tree Node Dialog width를 리턴한다.
	 * return Tree Node Dialog width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Tree Node Dialog width를 설정한다.
	 * @param width Tree Node Dialog width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Tree Node Dialog height를 리턴한다.
	 * return Tree Node Dialog height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Tree Node Dialog height를 설정한다.
	 * @param width Tree Node Dialog height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Tree Node get refer file path
	 * return file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Tree Node set refer file path
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * TreeNode 명과 Tree Node 설명을 Merge하여 리턴한다. 
	 * @return TreeNode 명과 Tree Node 설명을 Merge한 String
	 */
	public String getValue()
	{
		String message = "";
		
		if(desc.trim().equals(""))
		{
			message = name;
		}
		else
		{
			message = name + "\n" + desc;
		}
		
		return message;
	}
	
	/**
	 * ClipBoard에 복사할 Text를 리터한다.
	 * @return Tree Node 명
	 */
	public String getClipBoardText()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		TreeNode treeNode = (TreeNode)super.clone();
		
		return treeNode;
	}
}
