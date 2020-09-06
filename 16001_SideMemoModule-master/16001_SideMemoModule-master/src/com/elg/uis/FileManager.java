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
	 * TreeNode�� Ű�� ���� ���� ����
	 * @return Tree Node id
	 */
	public int getKey() {
		
		return key++;
	}

	/**
	 * ������
	 */
	private FileManager()
	{
		initQueue();
		
		initKey();
	}

	/**
	 * fileManager�� �����ϴ� �̱��� �޼ҵ�
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
	 * Wirte Queue�� �ʱ�ȭ�Ѵ�.
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
	* <p> Description: XML File�� �������� QUEUE ����Ʋ����</p>
	* </pre>
	*
	* @author Cheol Jong Park
	* @created: 2015.06
	* @modified:
	*
	 */
	class FileWriteQueueWoker extends QueueWorker {
		
		/**
		 * ������
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
	* <p> Description: XML File�� ���� key���� �������� QUEUE ����Ʋ����</p>
	* </pre>
	*
	* @author Cheol Jong Park
	* @created: 2015.06
	* @modified:
	*
	 */
	class KeyFileWriteQueueWoker extends QueueWorker {
		
		/**
		 * ������
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
	* <p> Description: File Wirte Bean ����Ʋ����</p>
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
		 * ������
		 * @param filePath XML FILE PATH
		 * @param fileName XML FILE Name
		 * @param treeModel Tree��
		 */
		public FileWriteInfo(String filePath, String fileName, TreeModel treeModel) {
			this.filePath = filePath;
			this.fileName = fileName;
			this.treeModel = treeModel;
		}
		
		/**
		 * ������
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
		 * FILE Path�� �����Ѵ�. 
		 * @return FILE Path 
		 */
		public String getFilePath() {
			return filePath;
		}

		/**
		 * FILE���� �����Ѵ�.
		 * @return FileName
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Tree���� �����Ѵ�.
		 * @return Tree��
		 */
		public TreeModel getTreeModel() {
			return treeModel;
		}

		/**
		 * TreeNode�� id�� �����Ѵ�.
		 * @return Tree Node id
		 */
		public int getKey() {
			return key;
		}
	}
	
}
