package com.elg.uis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
* <pre>
* <p> Title: DNDTree.java </p>
* <p> Description: Drag/Drap Tree 클래스</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class DNDTree extends JTree {
	public static final String FILTER_NAME = "필터:";
	private final String ACTION_DELETE = "DELETE";
	private final String ACTION_UPDATE = "UPDATE";
	
	private Insets autoscrollInsets = new Insets(20, 20, 20, 20); // insets

	private JPopupMenu popupMenu;
	private JMenuItem addMenuItem;
	private JMenuItem updateMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem removeFilterMenuItem;
	private JMenuItem backColorMenuItem;
	private JMenuItem foreColorMenuItem;

	private DNDTree tree;
	private DefaultTreeModel treeModel;
	private MemoPanel memoPanel;
	private JColorChooser backgroundColorChooser = new JColorChooser(); 
	private JColorChooser foregroundColorChooser = new JColorChooser(); 
	
	//XML 파일명
	private String fileName;
	//팝업메뉴를 선택한 TreeNode의 TreePath
	private TreePath selectedTreePath;
	
	//마우스 클릭시 선택한 TreeNode
	private DefaultMutableTreeNode selectTreeNode = null;


	//Shift + 더블클릭 : Edit Memo Dialog Open
	private boolean isShiftKeyPress = false;
	
	//Ctrl + 더블클릭   : 파일 탐색기 Open
	private boolean isCtrlKeyPress = false;
	
	/**
	 * 생성자
	 * @param rootName Root TreeNode명
	 * @param prarmFileName XML 파일명
	 * @param memoPanel MemoPanel 레퍼런스
	 */
	public DNDTree(String rootName, String prarmFileName, MemoPanel memoPanel) {
		this.memoPanel = memoPanel;
		this.fileName = prarmFileName;
		tree = this;
		
		//Tree Model을 초기화한다.
		initModel(rootName);
		//Tree를 초기화한다.
		initTree();
		//popup 메뉴를 초기화한다.
		initPopupMenu();
		//Clipboard를 초기홯나다.
		initClipboard();
	}


	/**
	 * Tree Model을 초기화한다.
	 * @param rootName Root TreeNode명
	 */
	private void initModel(String rootName)
	{
		DefaultMutableTreeNode rootTreeNode = null;
		
		try
		{
			String filePath = MemoFrame.FILE_PATH;
			//XML 파일이 있으면 XML파일에서 가지고 온다.
			rootTreeNode = FileManager.getInstance().read(filePath, fileName);
		}
		catch(Exception e)
		{
			rootTreeNode = null;
		}
		
		//파일이 없으면 root만 만들어서 넣어준다.
		if(rootTreeNode == null)
		{
			TreeNode treeNode = new TreeNode(rootName, "");
			rootTreeNode = new DefaultMutableTreeNode(treeNode);
		}
		
		treeModel = new  DefaultTreeModel(rootTreeNode);
		
		this.setModel(treeModel);
	}

	/**
	 * Tree를 초기화한다.
	 */
	private void initTree()
	{
		//Renderer를 설정한다.
		CustomTreeCellRenderer customTreeCellRenderer = new CustomTreeCellRenderer();
		customTreeCellRenderer.setBackgroundNonSelectionColor(MemoFrame.BACKGROUND_COLOR);

		int treeFontSize = MemoFrame.TREE_FONT_SIZE;
		Font font = new Font("굴림", Font.PLAIN, treeFontSize);
		customTreeCellRenderer.setFont(font);
        this.setCellRenderer(customTreeCellRenderer);

        //Enable tool tips.
        ToolTipManager.sharedInstance().registerComponent(this);

        this.setAutoscrolls(true);
		this.setRootVisible(true); 
		this.setShowsRootHandles(false);//to show the root icon
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION); //set single selection for the Tree
		this.setEditable(false);
		
		treeModel.reload();
		setExtandRowsAll();
		
		//Drag/Drop의 Handler를 등록한다.
		new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE);
		
		tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) { }
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@SuppressWarnings("null")
			@Override
			public void mouseClicked(MouseEvent e) {
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(e.getClickCount() == 1)
				{
					if(node == null)
					{
						return;
					}
					
					selectTreeNode = node;
					selectedTreePath = tree.getSelectionModel().getSelectionPath();
					
					TreeNode treeNode = (TreeNode)node.getUserObject();
	//				String value = treeNode.getValue();
					String value = treeNode.toString();
					String desc = treeNode.getDesc();
					memoPanel.setTextAreaValue(value, desc);
				}
				else if(e.getClickCount() == 2)
				{
					//Shift + 더블클릭 : Edit Memo Dialog Open
					if(isShiftKeyPress == true && isCtrlKeyPress == false)
					{
						TreeNode treeNode = (TreeNode)selectTreeNode.getUserObject();
						String value = treeNode.toString();
						
						if(!value.startsWith(FILTER_NAME)) {
							openEditMemoDialog();
						}
					}
					//Ctrl + 더블클릭   : 파일 탐색기 Open
					else if(isShiftKeyPress == false && isCtrlKeyPress == true)
					{
						TreeNode treeNode = (TreeNode)selectTreeNode.getUserObject();
						String referFilePath = treeNode.getFilePath();
						
						if(!"".equals(referFilePath)) {
							 try {        
				                Runtime runtime = Runtime.getRuntime();        
				                runtime.exec("explorer.exe "+referFilePath);        
				                
				            } catch (Exception ex) {
				            	ex.printStackTrace();
				            }
						}
					}
				}
			}
		});
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
          public boolean dispatchKeyEvent(KeyEvent e) {
            // This example converts all typed keys to upper
            // case
        	if (e.getID() == KeyEvent.KEY_TYPED) 
            {
            }
        	else if(e.getID() == KeyEvent.KEY_PRESSED)
        	{
        		char keyChar = e.getKeyChar();
        		int keyCode = e.getKeyCode();
        		
        		if(keyCode == KeyEvent.VK_SHIFT)
        		{
        			isShiftKeyPress = true;
        		}
        		else if(keyCode == KeyEvent.VK_CONTROL)
        		{
        			isCtrlKeyPress = true;
        		}
        	}
        	else if(e.getID() == KeyEvent.KEY_RELEASED)
        	{
        		char keyChar = e.getKeyChar();
        		int keyCode = e.getKeyCode();
        		
        		if(keyCode == KeyEvent.VK_SHIFT)
        		{
        			isShiftKeyPress = false;
        		}
        		else if(keyCode == KeyEvent.VK_CONTROL)
        		{
        			isCtrlKeyPress = false;
        		}
        	}
        	
        	// If the key should not be dispatched to the
            // focused component, set discardEvent to true
            boolean discardEvent = false;
            return discardEvent;
          }
        });
	}
	

	/**
	 * popup 메뉴를 초기화한다.
	 */
	private void initPopupMenu()
	{
		popupMenu = new JPopupMenu();
	 	addMenuItem = new JMenuItem();
		updateMenuItem = new JMenuItem();
		deleteMenuItem = new JMenuItem();
		removeFilterMenuItem = new JMenuItem();
		backColorMenuItem = new JMenuItem();
		foreColorMenuItem = new JMenuItem();
		
		//ADD 
		addMenuItem.setText("Add2");
		addMenuItem.setFont(new Font("Arial", 0, 14));
		addMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String name = "";
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
					
					name += "" + dateFormat.format(date) + " NEW Record";
					String desc = "Created Date : " + dateFormat.format(date) + "\n";
					
					int treeNodeKey = FileManager.getInstance().getKey();
					
					TreeNode treeNode = new TreeNode(treeNodeKey, name, desc);
					tree.addTreeNode(treeNode);
					
					//File에 write한다.
					String filePath = MemoFrame.FILE_PATH;
					FileManager.getInstance().write(filePath, fileName, tree.getModel());
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		
		//Update
		updateMenuItem.setText("Edit Memo");
		updateMenuItem.setFont(new Font("Lucida Regular", 0, 12));
		updateMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openEditMemoDialog();
			}
		});
		
		//Delete
		deleteMenuItem.setText("Delete2");
		deleteMenuItem.setFont(new Font("Lucida Regular", 0, 12));
//		deleteMenuItem.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				try
//				{
//					memoPanel.setTextAreaValue("", "");
//					
//					DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectedTreePath.getLastPathComponent();
//					TreeNode treeNode = (TreeNode)node.getUserObject();
//					
//			
//					// 변경되는 노드가 필터에 포함되어있는지 체크한다.
//					int key = treeNode.getKey();
//					List<DefaultMutableTreeNode> nodeList = searchNode(ACTION_DELETE, key);
//					
//					for(int i=0; i<nodeList.size(); i++) {
//						DefaultMutableTreeNode parentTreeNode = nodeList.get(i);
//						
//						for(int j=0; j<parentTreeNode.getChildCount(); j++) {
//							DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)parentTreeNode.getChildAt(j);
//							TreeNode childTreeNode = (TreeNode)childNode.getUserObject();
//							
//							if(childTreeNode.getKey() == key) {
//								treeModel.removeNodeFromParent(childNode);
//							}
//						}
//					}
//
//					//File에 write한다.
//	    			String filePath = MemoFrame.FILE_PATH;
//	    			FileManager.getInstance().write(filePath, fileName, tree.getModel());
//					
//				}
//				catch(Exception ex)
//				{
//					ex.printStackTrace();
//				}
//			}
//		});
		
		//Update
		removeFilterMenuItem.setText("Remove Filter");
		removeFilterMenuItem.setFont(new Font("Lucida Regular", 0, 12));
		removeFilterMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectedTreePath.getLastPathComponent();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();

				TreeNode treeNode = (TreeNode)node.getUserObject();
				TreeNode parentTreeNode = (TreeNode)parentNode.getUserObject();
				
				String parentName = parentTreeNode.getName();
				String desc = treeNode.getDesc();
				
				String filterName = parentName.replaceAll(FILTER_NAME, "");
				
				if(parentName.startsWith(FILTER_NAME)) {
					
					String match = "[" + filterName + "]";
					
					String removeFilterDesc = desc.replaceAll(match, "");
					treeNode.setDesc(removeFilterDesc);
					
					updateTreeNode(node, treeNode, true);
				}
				
				
			}
		});
		
		//Background Color
		Action backgourndColorChooserAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener okListener = new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Color color = backgroundColorChooser.getColor();
						
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectedTreePath.getLastPathComponent();
						TreeNode treeNode = (TreeNode)node.getUserObject();
						int rgbColor = color.getRGB();

						treeNode.setBackgroundColor(rgbColor);
						
						treeModel.nodeChanged(node);
						tree.setSelectionPath(null);
						
						//File에 write한다.
		    			String filePath = MemoFrame.FILE_PATH;
		    			FileManager.getInstance().write(filePath, fileName, tree.getModel());
					}
				};
				
				
				JDialog dialog = JColorChooser.createDialog(DNDTree.this, "", true,
						backgroundColorChooser, okListener, null);
				dialog.setVisible(true);
			}
		};
		
		backColorMenuItem.setText("Background Color");
		backColorMenuItem.setFont(new Font("Lucida Regular", 0, 12));
		backColorMenuItem.addActionListener(backgourndColorChooserAction);
		
		//Foreground Color
		Action foregroundColorChooserAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListener okListener = new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Color color = foregroundColorChooser.getColor();
						
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectedTreePath.getLastPathComponent();
						TreeNode treeNode = (TreeNode)node.getUserObject();
						int rgbColor = color.getRGB();

						treeNode.setForegroundColor(rgbColor);
						
						treeModel.nodeChanged(node);
						//File에 write한다.
		    			String filePath = MemoFrame.FILE_PATH;
		    			FileManager.getInstance().write(filePath, fileName, tree.getModel());
					}
				};
				
				
				JDialog dialog = JColorChooser.createDialog(DNDTree.this, "", true,
						foregroundColorChooser, okListener, null);

				dialog.setVisible(true);
			}
		};
		foreColorMenuItem.setText("Foreground Color");
		foreColorMenuItem.setFont(new Font("Lucida Regular", 0, 12));
		foreColorMenuItem.addActionListener(foregroundColorChooserAction);
		
		popupMenu.add(deleteMenuItem);
		popupMenu.addSeparator();
		popupMenu.add(addMenuItem);
		popupMenu.add(updateMenuItem);
 		
 		System.out.println("PRINT DDD");
		popupMenu.addSeparator();
		popupMenu.add(foreColorMenuItem);
		popupMenu.add(backColorMenuItem);
	
		this.add(popupMenu);

		PopupTrigger popupTrigger = new PopupTrigger();
		this.addMouseListener(popupTrigger);
	}
		
	/**
	 * treeNode를 복사하기 위해 tree에 CopyHandler를 set 한다.
	 */
	/**
	 * Clipboard를 초기홯나다.
	 */
	private void initClipboard()
	{
		 tree.setTransferHandler(new CopyHandler());
	}

	/**
	 * EditMemoDialog를 Open한다.
	 */
	private void openEditMemoDialog()
	{
		try
		{
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			TreeNode treeNodeObject = (TreeNode) treeNode.getUserObject();
			
			int key = treeNodeObject.getKey();
			
			WindowInterface windowInterface = OpenWindowManager.getInstance().getWindowInterface(key); 

			if(null == windowInterface) {
				// 수정을 위한 Dialog 생성 및 출력
				TreeNodeInfoDialog treeNodeInfoDialog = new TreeNodeInfoDialog(DNDTree.this, treeNode);

				OpenWindowManager.getInstance().add(treeNodeInfoDialog);
				
				treeNodeInfoDialog.setVisible(true);
				
			} else {
				windowInterface.toFrontDialog();
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 *  ParentNode에 등록됭 ChildNode중 name(TreeNode명)과 동일한 ChildNode를 리턴한다.
	 * @param parentNode Parent TreeNode
	 * @param name TreeNode명
	 * @return DefaultMutableTreeNode
	 */
	private DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode parentNode, String name)
	{
		DefaultMutableTreeNode treeNode = null;
		for(int i=0;i<this.getModel().getChildCount(parentNode);i++){
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)this.getModel().getChild(parentNode, i);
			String nameTmp = child.toString();

			if(nameTmp.equals(name))
			{
				treeNode = child;
				break;
			}
			else
			{
				treeNode = getTreeNode(child, name);
				if(treeNode != null)
				{
					break;
				}
			}
		}
		
		return treeNode;
	}
	
	
	/**
	 * 선택한 TreeNode에 자식노드를 등록한다. 
	 * @param node Tree노드정보
	 */
	public void addTreeNode(TreeNode node)
	{
		try
		{
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
			TreePath selectionTreeNode = tree.getSelectionModel().getSelectionPath();
			if(selectionTreeNode != null)
			{
				DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode)selectionTreeNode.getLastPathComponent();	
				treeModel.insertNodeInto(treeNode,parentTreeNode,parentTreeNode.getChildCount());
			}

			TreePath treePath = new TreePath(treeNode.getPath());
			tree.setSelectionPath(treePath);

			//부모를 selection한다.
			tree.setSelectionPath(selectionTreeNode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 선택한 노드의 정보중 name과 desc를 수정한다.
	 * @param name TreeNode 명
	 * @param desc TreeNode 설명
	 */
	public void updateTreeNode(String name, String desc)
	{
		try
		{
//			TreePath newSelectTreePath = tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode rootTreeNode =  (DefaultMutableTreeNode)treeModel.getRoot();
        	
        	if(rootTreeNode.equals(selectTreeNode))
        	{
        		return;
        		
        	} 
        	
        	if(null == selectTreeNode) {
        		return ;
        		
        	}
        	
        	//name일 "필터:"이면
        	//자식노드가 있으멸 retrun
        	if(name.startsWith(DNDTree.FILTER_NAME))
        	{
        		Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
				
				desc = "Created Date : " + dateFormat.format(date) + "\n";
        				
        		if(selectTreeNode.isLeaf() == false)
        		{
        			return;
        		}
        	}
        	
			TreeNode changeTreeNode = (TreeNode)selectTreeNode.getUserObject();
			// 변경되는 노드가 필터에 포함되어있는지 체크한다.
			List<DefaultMutableTreeNode> nodeList = searchNode(ACTION_UPDATE, changeTreeNode.getKey());
			for(int i=0; i<nodeList.size(); i++) {

				TreeNode treeNode = (TreeNode)nodeList.get(i).getUserObject();
				treeNode.setName(name);
				treeNode.setDesc(desc);
			}
			
			// 트리를 재구성한다.
			if(name.startsWith(DNDTree.FILTER_NAME))
        	{
				reloadDNDTree();
        	}

			//File에 write한다.
			String filePath = MemoFrame.FILE_PATH;
			FileManager.getInstance().write(filePath, fileName, tree.getModel());
			
			int value = memoPanel.getVerticalScrollBarValue();	
			ArrayList<TreePath> extendList = getExtandRows();
			treeModel.reload();
			setExtandRows(extendList, value);
		
			tree.setSelectionPath(selectedTreePath);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * TreeNode정보를 변경한다.
	 * @param defaultMutableTreeNode 선택노드
	 * @param name 노드명
	 * @param desc 노드 설명
	 */
	public void updateTreeNode(DefaultMutableTreeNode defaultMutableTreeNode, String name, String desc)
	{
		try
		{

        	
			TreeNode changeTreeNode = (TreeNode)defaultMutableTreeNode.getUserObject();
			// 변경되는 노드가 필터에 포함되어있는지 체크한다.
			List<DefaultMutableTreeNode> nodeList = searchNode(ACTION_UPDATE, changeTreeNode.getKey());
			for(int i=0; i<nodeList.size(); i++) {

				TreeNode treeNode = (TreeNode)nodeList.get(i).getUserObject();
				treeNode.setName(name);
				treeNode.setDesc(desc);
			}
			
			reloadDNDTree();

			//File에 write한다.
			String filePath = MemoFrame.FILE_PATH;
			FileManager.getInstance().write(filePath, fileName, tree.getModel());
			
		
			int value = memoPanel.getVerticalScrollBarValue();	
			ArrayList<TreePath> extendList = getExtandRows();
			treeModel.reload();
			setExtandRows(extendList, value);

			tree.setSelectionPath(selectedTreePath);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	/**
	 * Tree Node 의 속성 x, y, width, height를 수정한다.
	 * @param updateTreeNode x, y, width, height의 값을 가지고 있는 TreeNode
	 */
	public void updateTreeNode(DefaultMutableTreeNode selectedTreeNode, 
			TreeNode updateTreeNode, boolean isReload)
	{
		try
		{
			if(updateTreeNode.getName().startsWith(FILTER_NAME)) {
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
				
				String desc = "Created Date : " + dateFormat.format(date) + "\n";
				
				updateTreeNode.setDesc(desc);
			}
			
			TreeNode changeTreeNode = (TreeNode)selectedTreeNode.getUserObject();

			// 변경되는 노드가 필터에 포함되어있는지 체크한다.
			List<DefaultMutableTreeNode> nodeList = searchNode(ACTION_UPDATE, changeTreeNode.getKey());
			
			
			for(int i=0; i<nodeList.size(); i++) {
				DefaultMutableTreeNode updeateNode = nodeList.get(i);
				
				TreeNode treeNode = (TreeNode)updeateNode.getUserObject();
				
				treeNode.setName(updateTreeNode.getName());
				treeNode.setDesc(updateTreeNode.getDesc());
				treeNode.setX(updateTreeNode.getX());
				treeNode.setY(updateTreeNode.getY());
				treeNode.setWidth(updateTreeNode.getWidth());
				treeNode.setHeight(updateTreeNode.getHeight());
				
				treeNode.setFilePath(updateTreeNode.getFilePath());
			}
			
			
			if(isReload == true)
			{
				// 트리를 재구성한다.
				reloadDNDTree();
				
				int value = memoPanel.getVerticalScrollBarValue();	
				ArrayList<TreePath> extendList = getExtandRows();
				treeModel.reload();
				setExtandRows(extendList,value);
			}
						
			//File에 write한다.
			String filePath = MemoFrame.FILE_PATH;
			FileManager.getInstance().write(filePath, fileName, tree.getModel());
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * XML파일명을 리턴한다.
	 * @return XML파일명
	 */
	public String getFileName()
	{
		return fileName;
	}
	
    /**
     * 현재 extend된 노드들의 Row값을 extandRowVector에 저장한다.
     */
	public ArrayList<TreePath> getExtandRows()
    {
		ArrayList<TreePath> expendList = new ArrayList<TreePath>();
		
		DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
		@SuppressWarnings("unchecked")
	    Enumeration<DefaultMutableTreeNode> enumeration = rootTreeNode.depthFirstEnumeration();
		while (enumeration.hasMoreElements()) {
	        DefaultMutableTreeNode node = enumeration.nextElement();
	        TreePath treePath = new TreePath(node.getPath());

	        if(this.isExpanded(treePath))
	        {
	        	expendList.add(treePath);
	        }
		}
		
		return expendList;

    }


    /**
     * extandRowVector에 저장된 Row들을 extand한다. 
     */
	public void setExtandRows(ArrayList<TreePath> expendList, int value)
    {
		TreeExpandRunnable treeExpandRunnable = new TreeExpandRunnable(expendList, value);
		SwingUtilities.invokeLater(treeExpandRunnable);

    }

	/**
    * 모든 Node를 extand한다.
    */
	private void setExtandRowsAll()
    {
    	 for (int i = 0; i < this.getRowCount(); i++)
         {
    		   this.expandRow(i);

         }
    }

    
	/**
	 * scrool을 설정한다.
	 * @param cursorLocation cusor의 위치
	 */
	public void autoscroll(Point cursorLocation)  {
		Insets insets = getAutoscrollInsets();
		Rectangle outer = getVisibleRect();
		Rectangle inner = new Rectangle(outer.x+insets.left, outer.y+insets.top, outer.width-(insets.left+insets.right), outer.height-(insets.top+insets.bottom));
		if (!inner.contains(cursorLocation))  {
			Rectangle scrollRect = new Rectangle(cursorLocation.x-insets.left, cursorLocation.y-insets.top,     insets.left+insets.right, insets.top+insets.bottom);
			this.scrollRectToVisible(scrollRect);
		}
	}
	
	/**
	 * default scrollInset을 리턴한다.
	 * @return autoscrollInsets
	 */
	public Insets getAutoscrollInsets()  {
		return (autoscrollInsets);
	}

	/**
	 * DefaultMutableTreeNode를 객체복사한다.
	 * @param node DefaultMutableTreeNode
	 * @return 복사된 DefaultMutableTreeNode
	 */
	public static DefaultMutableTreeNode makeDeepCopy(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.getUserObject());
		for (Enumeration e = node.children(); e.hasMoreElements();) {     
			copy.add(makeDeepCopy((DefaultMutableTreeNode)e.nextElement()));
		}
		return(copy);
	}
	
	/**
	 * 필터노드 생성 시 트리를 재구성한다.
	 * @return 재구성된 DefaultMutableTreeNode
	 */
	private void reloadDNDTree() {
		try {
			DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
			
			@SuppressWarnings("unchecked")
		    Enumeration<DefaultMutableTreeNode> enumeration = rootTreeNode.depthFirstEnumeration();

			// 필터 노드 체크
			ArrayList<TreePath> expendList = new ArrayList<TreePath>();
			
			
			while (enumeration.hasMoreElements()) {
		        DefaultMutableTreeNode node = enumeration.nextElement();
		        
		        TreeNode  filterNode = (TreeNode)node.getUserObject();
		        String filterName = filterNode.getName();
		        
		        if(filterName.startsWith(FILTER_NAME)) {
			    	TreePath treePath = new TreePath(node.getPath());

		        	if(this.isExpanded(treePath))
		        	{
		        		expendList.add(treePath);
		        	}
		        	
		        	node.removeAllChildren();
		        }
			}
			
			
			@SuppressWarnings("unchecked")
		    Enumeration<DefaultMutableTreeNode> enumeration2 = rootTreeNode.depthFirstEnumeration();
			
			while (enumeration2.hasMoreElements()) {
		        DefaultMutableTreeNode node = enumeration2.nextElement();
		        
		        TreeNode  filterNode = (TreeNode)node.getUserObject();
		        String filterName = filterNode.getName();
		        
		        if(filterName.startsWith(FILTER_NAME)) {
			    	TreePath treePath = new TreePath(node.getPath());
		        	// 1. 필터에 해당하는 메모 검색
		        	List<DefaultMutableTreeNode> nodeList = searchNode(filterName);
		        	// 2. 복사한 객체에 검색된 노드 add
		        	for(int i=0; i<nodeList.size(); i++) {
		        		DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)nodeList.get(i);
		        		
		        		TreeNode treeNode = (TreeNode)childNode.getUserObject();
		        		
		        		boolean flag = checkInsertedKey(node, treeNode.getKey());

		    			if(flag) {
		    				
		    				childNode.removeAllChildren();
		    				treeModel.insertNodeInto(childNode,node,node.getChildCount());
		    			}

		        	}
		        }
		    }
			int value = memoPanel.getVerticalScrollBarValue();	
			TreeExpandRunnable treeExpandRunnable = new TreeExpandRunnable(expendList, value);
			SwingUtilities.invokeLater(treeExpandRunnable);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 필터에 자식노드를 insert한다.
	 * @param selectedDefaultMutableTreeNode 선택된 필터노드
	 */
	private void insertFilterChildNode(DefaultMutableTreeNode selectedDefaultMutableTreeNode) {
		try {
			
			TreeNode  filterNode = (TreeNode)selectedDefaultMutableTreeNode.getUserObject();
	        String name = filterNode.getName();

	        List<DefaultMutableTreeNode> nodeList = searchNode(name);
			
	    	for(int i=0; i<nodeList.size(); i++) {
	    		
	    		DefaultMutableTreeNode childDefaultMutableTreeNode = nodeList.get(i); 
	    		TreeNode childTreeNode = (TreeNode)childDefaultMutableTreeNode.getUserObject();
				
	    		
	    		boolean flag = checkInsertedKey(selectedDefaultMutableTreeNode, childTreeNode.getKey());

    			if(flag) {
    			treeModel.insertNodeInto(childDefaultMutableTreeNode,
    					selectedDefaultMutableTreeNode,
    					selectedDefaultMutableTreeNode.getChildCount());
	    	
    			}
	    	}
	    	
	    	TreePath treePath = new TreePath(selectedDefaultMutableTreeNode.getPath());
			tree.scrollPathToVisible(treePath);
			tree.expandPath(treePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 필터노드에 포함되어 있는 메모인지 확인한다.
	 * @param treeNode 필터노드
	 * @param key 포함해야할 노드의 key
	 * @return
	 */
	private boolean checkInsertedKey(DefaultMutableTreeNode treeNode, int key) {
		boolean flag = true;
		
		for(int k=0; k<treeNode.getChildCount(); k++) {
			DefaultMutableTreeNode insertedNode = (DefaultMutableTreeNode)treeNode.getChildAt(k);
			TreeNode insertedTreeNode = (TreeNode)insertedNode.getUserObject();
			
			if(key == insertedTreeNode.getKey()) {
				flag = false;
				
			}
		}
		
		return flag;
	}
	
	/**
	 * 필터명을 받아 해당하는 노드를 모두 검색한다.
	 * @param filterName filter명
	 * @return filter에 해당하는 TreeNode List
	 */
	private List<DefaultMutableTreeNode> searchNode(String filterName) {
		List<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
		
		DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
		
		@SuppressWarnings("unchecked")
	    Enumeration<DefaultMutableTreeNode> enumeration = rootTreeNode.depthFirstEnumeration();
		
		String filter = filterName.replaceAll(FILTER_NAME, "");
		
		while (enumeration.hasMoreElements()) {
	        DefaultMutableTreeNode node = enumeration.nextElement();
	        TreeNode treeNode = (TreeNode)node.getUserObject();
	        
	        String selectNodeDesc = treeNode.getDesc();
	        
	        if(selectNodeDesc.contains(filter)) {
	        	DefaultMutableTreeNode copyNode = (DefaultMutableTreeNode)makeDeepCopy(node);
	        	
	        	nodeList.add(copyNode);
	        }
		}
		
		return nodeList;
	}
	
	/**
	 * 키를 받아 해당하는 노드를 모두 검색한다.
	 * @param key node key
	 * @return 동일한 key를 가지고있는 해당하는 TreeNode List
	 */
	private List<DefaultMutableTreeNode> searchNode(String action, int key) {
		List<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
		
		DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
		
		@SuppressWarnings("unchecked")
	    Enumeration<DefaultMutableTreeNode> enumeration = rootTreeNode.depthFirstEnumeration();
		
		while (enumeration.hasMoreElements()) {
	        DefaultMutableTreeNode node = enumeration.nextElement();
	        TreeNode treeNode = (TreeNode)node.getUserObject();
	        
	        int selectNodeKey = treeNode.getKey();
	        
	        if(selectNodeKey == key) {
	        	DefaultMutableTreeNode copyNode = null;
	        	
	        	if(ACTION_UPDATE == action) {
	        		copyNode = (DefaultMutableTreeNode)makeDeepCopy(node);
	        		
	        	} else if(ACTION_DELETE ==action) {
	        		copyNode = (DefaultMutableTreeNode)node.getParent();
	        	}
	        	
	        	nodeList.add(copyNode);
	        }
		}
		
		return nodeList;
	}

	/**
	 * 현재 스크롤의 위치값을 가지고온다.
	 * @return 스크롤 위치값
	 */
	public int getVerticalScrollBarValue()
	{
		int value = memoPanel.getVerticalScrollBarValue();
		return value;
	}
	
    /**
     * 
    * <pre>
    * <p> Title: DNDTree.java </p>
    * <p> Description: PopupMenu를 show하는 MouseAdapter 내부클래스</p>
    * </pre>
    *
    * @author Cheol Jong Park
    * @created: 2015.06
    * @modified:
    *
     */
	class PopupTrigger extends MouseAdapter
    {
        public void mouseReleased(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                int x = e.getX();
                int y = e.getY();
                TreePath path = tree.getPathForLocation(x, y);

                if (path != null)
                {
                	DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                	DefaultMutableTreeNode rootTreeNode =  (DefaultMutableTreeNode)treeModel.getRoot();
                	
                	popupMenu.removeAll();
                	
                	if(rootTreeNode.equals(node)) 
                	{
                		popupMenu.add(addMenuItem);
                	}
                	else
                	{
                		String name = ((TreeNode)node.getUserObject()).getName();
                		
                		// 필터 노드일 때
                		if(name.contains(FILTER_NAME)) {
//                			popupMenu.add(removeMenuItem);

                		} else {
                			DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode)node.getParent();
                			String parentName = ((TreeNode)parentTreeNode.getUserObject()).getName();
                			
                			// 부모 노드가 필터 노드일 때
                			if(parentName.contains(FILTER_NAME)) {
		                		popupMenu.add(updateMenuItem);
//		                		popupMenu.add(removeMenuItem);
		                		popupMenu.addSeparator();
		                		popupMenu.add(removeFilterMenuItem);
		                		
                			} else {
		                		popupMenu.add(addMenuItem);
		                		popupMenu.add(updateMenuItem);
 		                		popupMenu.add(deleteMenuItem);
                			}
                			
                    		popupMenu.addSeparator();
                		}

//                		popupMenu.addSeparator();
                		popupMenu.add(foreColorMenuItem);
                		popupMenu.add(backColorMenuItem);
                	}
                	
                	selectedTreePath = path;
                	selectTreeNode = node;
                	
                	tree.setSelectionPath(path);
            		popupMenu.show(tree, x, y);	
                }
            }
        }
    }    
    
    /**
     * 
    * <pre>
    * <p> Title: DNDTree.java </p>
    * <p> Description: Clipboard Handler 내부 클래스</p>
    * </pre>
    *
    * @author Cheol Jong Park
    * @created: 2015.06
    * @modified:
    *
     */
    class CopyHandler extends TransferHandler {
       
    	public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
            JTree tree = (JTree)comp;
            TreePath[] path = tree.getSelectionPaths();
            
            String clipboardContents = "";
            
            if(path != null) {
            	for(int i=0; i<path.length; i++) {

            		TreePath treePath = path[i];

            		int pathCount = treePath.getPathCount();
            		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            		TreeNode treeNode = (TreeNode)node.getUserObject();
            		
            		
            		String text = "";
            		

            		for(int j=0 ; j < pathCount ; j++)
            		{
            			if(j != 0)
            			{
            				text+= "  ";
            			}
            		}
            		text += treeNode.getClipBoardText();

            		
            		clipboardContents += text;
            		clipboardContents += "\n";
            		clip.setContents(new StringSelection(clipboardContents), null);
            	}
            }
        }
    }
    
     /**
     * 
    * <pre>
    * <p> Title: DNDTree.java </p>
    * <p> Description: TreeCellRenderer 내부 클래스</p>
    * </pre>
    *
    * @author Cheol Jong Park
    * @created: 2015.06
    * @modified:
    *
     */
	private class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
		public CustomTreeCellRenderer() {
		}

		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean sel,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(
					tree, value, sel,
					expanded, leaf, row,
					hasFocus);
			
			DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)value;
			
			TreeNode treeNode = (TreeNode)defaultMutableTreeNode.getUserObject();
			int backgroundColor = treeNode.getBackgroundColor();
			int foregroundColor = treeNode.getForegroundColor();
			
			this.setForeground(new Color(foregroundColor));
			
			if(sel) {
				this.setOpaque(false);
			} else {
				this.setOpaque(true);
				this.setBackground(new Color(backgroundColor));
			}
			
			String desc = treeNode.getDesc().trim();
			
			if(!desc.equals(""))
			{
				desc = treeNode.getDesc().replace("\n", "<br>");
				desc += "<br>" + treeNode.getFilePath();
				desc = "<html>" + desc + "</html>";

				this.setToolTipText(desc);
			}
			else
			{
				this.setToolTipText("");
			}
			return this;
		}
	}
	
	/**
	 * 
	* <pre>
	* <p> Title: DNDTree.java </p>
	* <p> Description: Tree를 Extend하고, Scroll을 조정하는  Runnable클래스</p>
	* </pre>
	*
	* @author Cheol Jong Park
	* @created: 2015.06
	* @modified:
	*
	 */
	class TreeExpandRunnable implements Runnable
	{
		
		private List<TreePath> extandsList = null;
		private int value;
		public TreeExpandRunnable(List<TreePath> extandList, int value)
		{
			this.extandsList = extandList;
			this.value = value;
		}
		
		public void run()
		{
			for(TreePath expandTreePath : extandsList)
	        {
	        	DNDTree.this.expandPath(expandTreePath);
	        }
			
			memoPanel.setVerticalScrollBarValue(value);
		}
	}
}