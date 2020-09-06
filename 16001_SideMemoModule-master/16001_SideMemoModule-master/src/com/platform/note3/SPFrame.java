package com.platform.note3;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class SPFrame extends JFrame {
	private DataTreeModel treeModel;

	public void init(DataTreeModel treeModel) {
		this.treeModel = treeModel;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 400;
		int height = (int) screenSize.getHeight() - 50;

		Dimension windowSize = new Dimension(width, height);
		this.setSize(windowSize);

		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		this.setLocation(screenSize.width - width, 0);
		this.setResizable(true);

		build(this.getContentPane());
		this.setTitle("즉시,반드시,끝까지,5Y,80:20,마감");

		// JEditorPane pan = new JEditorPane();
		// pan.setFont(new Font("맑은 고딕", 0, 15));
		// pan.setText(new SPExcelLoader().test2());

		this.setVisible(true);
	}

	public void build(Container container) {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("aaa", makeTab());
		container.add(BorderLayout.CENTER, tabbedPane);
	}

	public JComponent makeTab() {
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		SPTree mTree = new SPTree(treeModel);
		{
			JTextField textNew = new JTextField();
			textNew.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						System.out.println("Enter: " + textNew.getText());
						mTree.quickCreateNode(textNew.getText());
						textNew.setText("");

					}
				}

			});
			container.add(BorderLayout.NORTH, textNew);
		}
		{
			JEditorPane editPane = mTree.getEditPane();
			editPane.setPreferredSize(new Dimension(500, 100));
			editPane.setMaximumSize(new Dimension(500, 100));
			editPane.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						System.out.println("Enter: " + editPane.getText());
					}
				}
			});
			mTree.init();
			// mTree.setPreferredSize(new Dimension(600, 100));
			mTree.setMinimumSize(new Dimension(500, 100));
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mTree, editPane);
			splitPane.setContinuousLayout(true);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(500);
			splitPane.setDividerSize(12);
			splitPane.setPreferredSize(new Dimension(600, 100));
			container.add(BorderLayout.CENTER, splitPane);
		}

		return container;
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	public static final void main(String[] args) {
		//DataTreeModel treeModel = new StringTreeModel();
		 DataTreeModel treeModel = new TaskTreeModel();
		new SPFrame().init(treeModel);
	}
}
