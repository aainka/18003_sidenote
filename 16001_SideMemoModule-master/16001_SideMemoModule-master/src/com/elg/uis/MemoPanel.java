package com.elg.uis;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MemoPanel extends JPanel implements ActionListener{

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private GridBagLayout gridBagLayout4 = new GridBagLayout();
	
	private JPanel mainPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	
	private JTextField inputTextField = new JTextField();
	private JButton inputButton = new JButton();

	private JScrollPane centerPanelScrollPane;
	private DNDTree tree; 
	
	private String rootName;
	private String fileName;
	
	
	/**
	 * 각각의 메모 패널을 생성한다.
	 * @param rootName	메모 root (MemoFrame.TITLE_MEMO_1~5) 
	 * @param fileName	읽기/쓰기 할 파일 명 (MemoFrame.FILE_NAME_MEMO_1~5)
	 */
	public MemoPanel(String rootName, String fileName)
	{
		this.rootName = rootName;
		this.fileName = fileName;
		try
		{
			jbInit();
			initComponent();
			
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
		topPanel.setLayout(gridBagLayout3);
		centerPanel.setLayout(gridBagLayout4);

		this.setBackground(MemoFrame.BACKGROUND_COLOR);
		mainPanel.setBackground(MemoFrame.BACKGROUND_COLOR);
		topPanel.setBackground(MemoFrame.BACKGROUND_COLOR);
		centerPanel.setBackground(MemoFrame.BACKGROUND_COLOR);
		
		inputButton.setText("입력");
		inputButton.addActionListener(this);
		
		tree = new DNDTree(rootName, fileName, this);
		
		centerPanelScrollPane = new JScrollPane(tree);
		centerPanelScrollPane.getViewport().setBackground(MemoFrame.BACKGROUND_COLOR);

		tree.setBackground(MemoFrame.BACKGROUND_COLOR);
		inputTextField.setBackground(MemoFrame.BACKGROUND_COLOR);

		topPanel.add(inputTextField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		centerPanel.add(centerPanelScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		mainPanel.add(topPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(centerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
    			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}
	
	/**
	 * 사용자 action을 지정한다.
	 */
	private void initComponent()
	{
		inputTextField.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
	        {
				String desc = e.getActionCommand();
				executeUpdate(desc);
	        }
	    });
	}

	/**
	 * inputButton의 action을 지정한다.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(inputButton))
		{
			executeInputButton();
		}
	}
	
	/**
	 * 해당 메소드가 호출되면 변경된 메모의 내용이 파일에 update 된다.
	 */
	private void executeUpdate(String desc) {
		try {
			String name = inputTextField.getText();
			
			if(name.trim().equals(""))
			{
				return;
			}

			tree.updateTreeNode(name, desc);
			
			inputTextField.setText("");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 해당 메소드가 호출되면 변경된 메모의 내용이 파일에 insert 된다.
	 */
	private void executeInputButton()
	{
		try
		{
			String memoText = inputTextField.getText();
			if(memoText.trim().equals(""))
			{
				return;
			}
			
			String name = "";
			int treeNodeKey = FileManager.getInstance().getKey();
			
			TreeNode treeNode = new TreeNode(treeNodeKey, name);
			
			tree.addTreeNode(treeNode);
			
			//File에 write한다.
			String filePath = MemoFrame.FILE_PATH;
			FileManager.getInstance().write(filePath, fileName, tree.getModel());
			
			inputTextField.setText("");
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void setTextAreaValue(String value, String desc)
	{
		inputTextField.setText(value);
		
		if(value.startsWith(DNDTree.FILTER_NAME)) {
			inputTextField.setEnabled(false);
		} else {
			inputTextField.setEnabled(true);
		}
		
		inputTextField.setActionCommand(desc);
	}
	
	public int getVerticalScrollBarValue()
	{
		int value = centerPanelScrollPane.getVerticalScrollBar().getValue();
		
		int maximumValue = centerPanelScrollPane.getVerticalScrollBar().getMaximum();
		int scrollBarHeight = centerPanelScrollPane.getVerticalScrollBar().getHeight();
		
		if(value + scrollBarHeight == maximumValue)
		{
			value = -1;
		}
		
		return value;
	}

	public void setVerticalScrollBarValue(int value)
	{
		VerticalScrollBarSetValueRunnable verticalScrollBarSetValueRunnable = new VerticalScrollBarSetValueRunnable(value);
		SwingUtilities.invokeLater(verticalScrollBarSetValueRunnable);
	}

	
	class VerticalScrollBarSetValueRunnable implements Runnable
	{
		private int value;
		
		
		public VerticalScrollBarSetValueRunnable(int value)
		{
			this.value = value;
		
		}
		
		public void run()
		{
			if(value == -1)
			{
				centerPanelScrollPane.getVerticalScrollBar().setValue(centerPanelScrollPane.getVerticalScrollBar().getMaximum());
			}
			else
			{
				centerPanelScrollPane.getVerticalScrollBar().setValue(value);
			}
		}
	}
}
