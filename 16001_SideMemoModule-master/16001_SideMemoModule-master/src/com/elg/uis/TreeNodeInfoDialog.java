package com.elg.uis;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNodeInfoDialog extends JDialog implements ActionListener, WindowInterface{
	private final int DIALOG_DEFAULT_WIDTH = 300;
	private final int DIALOG_DEFAULT_HEIGHT = 200;
	
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private GridBagLayout gridBagLayout4 = new GridBagLayout();
	private GridBagLayout gridBagLayout5 = new GridBagLayout();
	private GridBagLayout gridBagLayout6 = new GridBagLayout();
	private GridBagLayout gridBagLayout7 = new GridBagLayout();
	
	private JPanel mainPanel = new JPanel();
	private JPanel infoPanel = new JPanel();
	private JPanel namePanel = new JPanel();
	private JPanel descPanel = new JPanel();
	private JPanel fileChoosePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private JTextField inputNameTextField = new JTextField();
	private JTextArea inputDescTextArea = new JTextArea();
	private JTextField inputFilePathTextField = new JTextField();
	
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	
	private JButton filePathChooseButton = new JButton();
	private JButton windowExplorerOpenButton = new JButton();

	private JScrollPane descPanelScrollPane;
	
	private DefaultMutableTreeNode selectTreeNode;
	
	private DNDTree dndTree;
	private TreeNode treeNode;
	
	private JFileChooser fileChooser;
	
	public TreeNodeInfoDialog(DNDTree dndTree, DefaultMutableTreeNode selectTreeNode) {
		super();
		
		try
		{
			TreeNode oriTreeNode = (TreeNode)selectTreeNode.getUserObject();
			treeNode = (TreeNode)oriTreeNode.clone();
			setTitle(treeNode.getName());
			this.dndTree = dndTree;
			this.selectTreeNode = selectTreeNode;

			jbInit();
			initComponent();
			initData();
			initDiaolgPosition();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * GUI를 초기화한다.
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);
		mainPanel.setLayout(gridBagLayout2);
		infoPanel.setLayout(gridBagLayout3);
		namePanel.setLayout(gridBagLayout4);
		descPanel.setLayout(gridBagLayout5);
		buttonPanel.setLayout(gridBagLayout6);
		fileChoosePanel.setLayout(gridBagLayout7);

		this.setBackground(MemoFrame.BACKGROUND_COLOR);
		mainPanel.setBackground(MemoFrame.BACKGROUND_COLOR);
		fileChoosePanel.setBackground(MemoFrame.BACKGROUND_COLOR);
		buttonPanel.setBackground(MemoFrame.BACKGROUND_COLOR);

		inputFilePathTextField.setEnabled(false);
		
		okButton.setText("확인");
		okButton.addActionListener(this);
		
		cancelButton.setText("취소");
		cancelButton.addActionListener(this);
		
		windowExplorerOpenButton.setText("열기");
		windowExplorerOpenButton.addActionListener(this);
		
		filePathChooseButton.setText("파일선택");
		filePathChooseButton.addActionListener(this);
		
		descPanelScrollPane = new JScrollPane(inputDescTextArea);
		descPanelScrollPane.getViewport().setBackground(MemoFrame.BACKGROUND_COLOR);

		namePanel.add(inputNameTextField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		descPanel.add(descPanelScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		fileChoosePanel.add(windowExplorerOpenButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		fileChoosePanel.add(inputFilePathTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		fileChoosePanel.add(filePathChooseButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		infoPanel.add(namePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		infoPanel.add(descPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		infoPanel.add(fileChoosePanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		buttonPanel.add(okButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
				
		buttonPanel.add(cancelButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		mainPanel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
    			GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}
	
	/**
	 * 사용자 action을 지정한다.
	 */
	private void initComponent()
	{
		TreeNodeInfoDialog.this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) { 
				inputDescTextArea.requestFocus();
			}
			
			/**
			 * component가 resize 될 때 호출된다.
			 */
			@Override
			public void componentResized(ComponentEvent e) { 
				
				Rectangle componentRectangle = e.getComponent().getBounds();

				treeNode.setWidth(componentRectangle.width);
				treeNode.setHeight(componentRectangle.height);
				
				dndTree.updateTreeNode(selectTreeNode, treeNode, false);
			}
			
			/**
			 * component가 move 될 때 호출 된다.
			 */
			@Override
			public void componentMoved(ComponentEvent e) {
				try {

					Rectangle componentRectangle = e.getComponent().getBounds();
					
					treeNode.setX(componentRectangle.x);
					treeNode.setY(componentRectangle.y);
					
					dndTree.updateTreeNode(selectTreeNode, treeNode, false);
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			@Override
			public void componentHidden(ComponentEvent e) { }
		});
	}
	
	/**
	 * 초기데이터를 입력한다.
	 */
	private void initData()
	{
		TreeNode treeNode = (TreeNode)selectTreeNode.getUserObject();
		
		String name = treeNode.getName();
		String desc = treeNode.getDesc();
		String referFilePath = treeNode.getFilePath();
		
		inputNameTextField.setText(name);
		inputDescTextArea.setText(desc);
		inputFilePathTextField.setText(referFilePath);
		
		inputDescTextArea.setCaretPosition(0);
		
	}
	
	/**
	 * 초기 사이즈 및 위치를 설정한다.
	 */
	private void initDiaolgPosition() {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        
	        // screen의 너비, 높이
	        int screenWidth = screenSize.width;
	        int screenHeight = screenSize.height;
	        
	        // 기본 적용될 Dialog의 크기
	        int defaultX = screenWidth/2 - DIALOG_DEFAULT_WIDTH/2;
	        int defaultY = screenHeight/2 - DIALOG_DEFAULT_HEIGHT/2;
	        
	        // 사용자 설정 x, y, width, height
	        int xPos = treeNode.getX();
	        int yPos = treeNode.getY();
	        
	        int width = treeNode.getWidth();
	        int height = treeNode.getHeight();
	        
	        if(xPos <= 0) {
	        	xPos = defaultX;
	        }
	        
			if(yPos <= 0) {
				yPos = defaultY;
			}
			
			if(xPos > screenWidth || yPos > screenHeight){
				xPos = defaultX;
				yPos = defaultY;
			}
			
			if(width < 0 || width < DIALOG_DEFAULT_WIDTH) {
				width = DIALOG_DEFAULT_WIDTH;
			}
			
			if(height < 0 || height < DIALOG_DEFAULT_HEIGHT) {
				height = DIALOG_DEFAULT_HEIGHT;
			}
			
			treeNode.setX(xPos);
			treeNode.setY(yPos);
			treeNode.setWidth(width);
			treeNode.setHeight(height);
			
			dndTree.updateTreeNode(selectTreeNode, treeNode, false);
	        
			
			this.setBounds(new Rectangle(xPos, yPos, width, height));
	        this.setResizable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	/**
	 * 사용자 action을 지정한다.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(okButton)) {
			String name = "";
			String desc = "";
			String filePath = "";
			
			name = inputNameTextField.getText();
			
			desc = inputDescTextArea.getText();
			
			filePath = inputFilePathTextField.getText();
			
			treeNode.setName(name);
			treeNode.setDesc(desc);
			
			treeNode.setFilePath(filePath);
			
			dndTree.updateTreeNode(selectTreeNode, treeNode, true);
			
			close();
			
		} else if(e.getSource().equals(filePathChooseButton)) {
		    fileChooser = new JFileChooser(); 
		    fileChooser.setCurrentDirectory(new java.io.File("."));
		    fileChooser.setDialogTitle("선택");
		    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		    // disable the "All files" option.
		    fileChooser.setAcceptAllFileFilterUsed(false);
		    //    
		    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
		      
		      inputFilePathTextField.setText(fileChooser.getSelectedFile().toString());
		    
		    }
			
		} else if(e.getSource().equals(windowExplorerOpenButton)) { 
            String path = inputFilePathTextField.getText();  
            
			if(!"".equals(path)) {

				try {              
	                Runtime runtime = Runtime.getRuntime();        
	                runtime.exec("explorer.exe "+path);        
	                
	            } catch (Exception ex) {
	            	ex.printStackTrace();
	            }
			}
			
		} else if(e.getSource().equals(cancelButton)) {
			close();
		}
	}
	
	

	@Override
	public int getKey() {
		int key = treeNode.getKey();
		return key;
	}

	@Override
	public void close() {
		OpenWindowManager.getInstance().remove(this);
		dispose();
	}

	@Override
	public void toFrontDialog() {
		this.toFront();
	}
	
	  
    @Override
    protected void processWindowEvent(WindowEvent e)
    {
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
        	close();
        }
    }
}

