package com.elg.uis;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * 
* <pre>
* <p> Title: FileManager.java </p>
* <p> Description: XML FILE read/write Manager</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class FileManager implements IFileManager{
	private final int DEFAULT_KEY = 1;

	private final String FILE_NAME_KEY = "key";
	
	private static volatile FileManager fileManager = null;
	private FileWriteQueueWoker fileWriteQueueWoker;
	private KeyFileWriteQueueWoker keyFileWriteQueueWoker;
	private XmlConvertUtil xmlConvertUtil = new XmlConvertUtil();
	
	private int key;
	
	/**
	 * TreeNode의 키로 사용될 값을 리턴
	 * @return Tree Node id
	 */
	public int getKey() {
		
		return key++;
	}

	/**
	 * 생성자
	 */
	private FileManager()
	{
		initQueue();
		
		initKey();
	}

	/**
	 * fileManager를 리턴하는 싱글톤 메소드
	 * @return fileManager
	 */
	public static FileManager getInstance() {
		if (null == fileManager) {
			synchronized (FileManager.class) {
				
				if(null == fileManager) {
					fileManager = new FileManager();
				}	
			}
		}

		return fileManager;
	}
	
	/**
	 * Wirte Queue를 초기화한다.
	 */
	private void initQueue()
	{
		fileWriteQueueWoker = new FileWriteQueueWoker();
		keyFileWriteQueueWoker = new KeyFileWriteQueueWoker();
		
		fileWriteQueueWoker.start();
		keyFileWriteQueueWoker.start();
	}
	
	private void initKey() {
		int key = DEFAULT_KEY;
		try {
			key = readKeyFile(MemoFrame.DEFAULT_FILE_PATH, FILE_NAME_KEY);
			
		} catch (Exception e) {
			writeKeyFile(MemoFrame.DEFAULT_FILE_PATH, FILE_NAME_KEY, key);
			e.printStackTrace();
		}
		
		this.key = key;
	}
	
	@Override
	public void write(String filePath, String fileName, TreeModel treeModel) {
		FileWriteInfo fileWriteInfo = new FileWriteInfo(filePath, fileName, treeModel);
		fileWriteQueueWoker.push(fileWriteInfo);
		
		writeKeyFile(filePath, FILE_NAME_KEY, key);
	}


	@Override
	public DefaultMutableTreeNode read(String filePath, String fileName)
			throws Exception {
		
		DefaultMutableTreeNode defaultMutableTreeNode = null;
		try
		{
			defaultMutableTreeNode = xmlConvertUtil.readFile(filePath, fileName);
		}
		catch(Exception e)
		{
			throw e;
		}

		// TODO Auto-generated method stub
		return defaultMutableTreeNode;
	}

	@Override
	public int readKeyFile(String filePath, String fileName) throws Exception {
		
		int key = 0;
		try {
			key = xmlConvertUtil.readKeyFile(filePath, fileName);
			
		} catch(Exception e) {
			throw e;
		}
		
		return key;
	}

	@Override
	public void writeKeyFile(String filePath, String fileName, int key) {
		FileWriteInfo keyFileWriteInfo = new FileWriteInfo(filePath, fileName, key);
		keyFileWriteQueueWoker.push(keyFileWriteInfo);
	}



	/**
	 * 
	* <pre>
	* <p> Title: FileManager.java </p>
	* <p> Description: XML File에 쓰기위한 QUEUE 내부틀래스</p>
	* </pre>
	*
	* @author Cheol Jong Park
	* @created: 2015.06
	* @modified:
	*
	 */
	class FileWriteQueueWoker extends QueueWorker {
		
		/**
		 * 생성자
		 */
		public FileWriteQueueWoker() {
		}

		@Override
		public void processObject(Object model) {
			try {
				FileWriteInfo fileWriteInfo = (FileWriteInfo) model;
				
				String filePath = fileWriteInfo.getFilePath(); 
				String fileName = fileWriteInfo.getFileName(); 
				TreeModel treeModel = fileWriteInfo.getTreeModel();
				
				
				xmlConvertUtil.writeFile(filePath, fileName, treeModel);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	* <pre>
	* <p> Title: FileManager.java </p>
	* <p> Description: XML File에 최종 key값을 쓰기위한 QUEUE 내부틀래스</p>
	* </pre>
	*
	* @author Cheol Jong Park
	* @created: 2015.06
	* @modified:
	*
	 */
	class KeyFileWriteQueueWoker extends QueueWorker {
		
		/**
		 * 생성자
		 */
		public KeyFileWriteQueueWoker() {
		}

		@Override
		public void processObject(Object model) {
			try {
				FileWriteInfo fileWriteInfo = (FileWriteInfo) model;
				
				String filePath = fileWriteInfo.getFilePath(); 
				String fileName = fileWriteInfo.getFileName(); 
				
				xmlConvertUtil.writeKeyFile(filePath, fileName, key);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	* <pre>
	* <p> Title: FileManager.java </p>
	* <p> Description: File Wirte Bean 내부틀래스</p>
	* </pre>
	*
	* @author Cheol Jong Park
	* @created: 2015.06
	* @modified:
	*
	 */
	class FileWriteInfo {
		private String filePath;
		private String fileName;
		private int key;
		private TreeModel treeModel;

		/**
		 * 생성자
		 * @param filePath XML FILE PATH
		 * @param fileName XML FILE Name
		 * @param treeModel Tree모델
		 */
		public FileWriteInfo(String filePath, String fileName, TreeModel treeModel) {
			this.filePath = filePath;
			this.fileName = fileName;
			this.treeModel = treeModel;
		}
		
		/**
		 * 생성자
		 * @param filePath XML FILE PATH
		 * @param fileName XML FILE Name
		 * @param key Tree Node id
		 */
		public FileWriteInfo(String filePath, String fileName, int key) {
			this.filePath = filePath;
			this.fileName = fileName;
			this.key = key;
		}

		/**
		 * FILE Path를 리턴한다. 
		 * @return FILE Path 
		 */
		public String getFilePath() {
			return filePath;
		}

		/**
		 * FILE명을 리턴한다.
		 * @return FileName
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Tree모델을 리턴한다.
		 * @return Tree모델
		 */
		public TreeModel getTreeModel() {
			return treeModel;
		}

		/**
		 * TreeNode의 id를 리턴한다.
		 * @return Tree Node id
		 */
		public int getKey() {
			return key;
		}
	}
	
}
