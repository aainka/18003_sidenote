package platform.sidenote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import platform.sidenote.util.TinyValue;

public class TaskTreeController implements TreeSelectionListener {

	private DataTreeModel treeModel = new TaskTreeModel();
	private SPTree treeView = new SPTree(treeModel);
	private JTextField textNew = new JTextField();
	private JTextPane noteEditor = new JTextPane();
	private DefaultMutableTreeNode fromNode = null;
	// Debug logger = Debug.getLogger(this.getClass());
	public boolean openNote = false;
//	private TinyToolbar buttonMenu;

	TinyToolbar menubar = new TinyToolbar() {
		@Override
		public void init() {
			String cmds = "(SAVE,save),(PRINT,print),(ETC,testETC)";
			List<String[]> valueList = TinyValue.parse(cmds);
			addMenu(valueList);
			TinyCallBackActionListener listener = new TinyCallBackActionListener(this, valueList);
			addActionListener(listener);
		}

		public void _save(Object source, Point p) {
			// updateNoteToNode();
			// int count = treeModel.saveNodes();
			// JOptionPane.showMessageDialog(null, "" + count + " recodes are saved");
			__save();
		}

		public void _WR(TaskTreeController controller, Point p) {
			try {
				Desktop.getDesktop().browse(new URI(
						"https://ericsson.sharepoint.com/sites/Network_PM_LM/Project%20Manager%20Meeting/Forms/AllItems.aspx?id=%2Fsites%2FNetwork_PM_LM%2FProject%20Manager%20Meeting%2FvEPG-SI"));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void printItemList() {
			JOptionPane.showMessageDialog(null, "Print");
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
			printItemSub(0, "", root);
			// Enumeration e = root.breadthFirstEnumeration();
			// while (e.hasMoreElements()) {
			// DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			// if (node.isLeaf())
			// continue;
			//// int row = treeView.getRowForPath(new TreePath(node.getPath()));
			//// treeView.expandRow(row);
			// }
		}

	};

	OT_Popup pp = new OT_Popup() {
		@Override
		public void init() {
			String cmds = "(Collapse,collaspse),(Priority,priority),(Title,title)";
			List<String[]> valueList = TinyValue.parse(cmds);
			addMenu(valueList);
			l = new TinyCallBackActionListener(this, OT_Popup.class, valueList);
			source = treeView;
			treeView.setComponentPopupMenu(this);
		}

		public void _setPriority(Object source, Point poped) {
			OV_Task task = treeView.getTaskAt(poped);
			if (task.priority > 0) {
				task.priority = 0;
			} else {
				task.priority = 1;
			}
			treeView.updateUI();
		}

		public void _title(OT_Popup popup, Point poped) {
			OV_Task task = treeView.getTaskAt(poped);
			task.priority = 6;
			treeView.updateUI();
		}
	};

	public TaskTreeController() {
		Document blank = new DefaultStyledDocument();
		noteEditor.setDocument(blank);
	}

	public TinyToolbar getMenubar() {
		return menubar;
	}

	public void __save() {
		updateNoteToNode();
		int count = treeModel.saveNodes();
		JOptionPane.showMessageDialog(null, "" + count + " recodes are saved");
	}

	// **************************************************************
	// *** Build User Interface
	// **************************************************************

	public JPanel buildUi() {
		treeView.addTreeSelectionListener(this);
		noteEditor.setPreferredSize(new Dimension(500, 100));
		noteEditor.setMaximumSize(new Dimension(500, 100));
		noteEditor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				valueChanged();
				// coloring();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					updateNoteToNode();

				}
			}
		});

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		textNew.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println("Enter: " + textNew.getText());
					quickCreateNode(textNew.getText());
					textNew.setText("");
					valueChanged();
				}
			}
		});
		container.add(BorderLayout.NORTH, textNew);
		{
			treeView.setMinimumSize(new Dimension(400, 100));
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, noteEditor);
			splitPane.setContinuousLayout(true);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(500);
			splitPane.setDividerSize(12);
			splitPane.setPreferredSize(new Dimension(600, 100));
			container.add(BorderLayout.CENTER, splitPane);
		}

		return container;
	}

	public void coloring() {
		StyledDocument doc = (StyledDocument) noteEditor.getStyledDocument();

		Style style = doc.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, Color.red);
		StyleConstants.setBackground(style, Color.YELLOW);
		try {
			doc.insertString(doc.getLength(), "BLAH ", style);
		} catch (BadLocationException ex) {
		}

		StyleConstants.setForeground(style, Color.blue);

		try {
			doc.insertString(doc.getLength(), "BLEH", style);
		} catch (BadLocationException e) {
		}
	}

	// public static void xx(String[] args) {
	// JTextPane textPane = new JTextPane();
	// StyledDocument doc = textPane.getStyledDocument();
	//
	//
	// StyleContext sc = StyleContext.getDefaultStyleContext();
	// AttributeSet aset = sc.addAttribute(
	// SimpleAttributeSet.EMPTY,
	// StyleConstants.Foreground, highlightColor);
	// cobolProgram.setCharacterAttributes(offset, length, aset,
	// false);
	//
	//
	// }

	// **************************************************************
	// *** EVENT Interface
	// **************************************************************

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		SPTree tree = (SPTree) e.getSource();
		DefaultMutableTreeNode toNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		changeSelection(fromNode, toNode);
		// treeModel.reload(fromNode);
		fromNode = toNode;

		// treeModel.reload(toNode);
		// treeModel.nodeStructureChanged(toNode);
	}

	int valueChangedCount = 0;

	public void valueChanged() {
		valueChangedCount++;
		menubar.valueChanged();
	}

	// **************************************************************
	// *** Action Implementation
	// **************************************************************

	public void quickCreateNode(String pSubject) {

		if (pSubject.indexOf("delete") == 0) {
			// treeModel.removeNodeFromParent(node);
			return;
		}
		// new creation
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
		if (parent == null) {
			parent = (DefaultMutableTreeNode) treeModel.getRoot();
		} else {
			System.out.println("add.parent=" + parent);
		}
		DefaultMutableTreeNode newChild = treeModel.createNode(parent, pSubject);

		treeView.updateUI();
		TreePath treePath = new TreePath(newChild.getPath());
		treeView.scrollPathToVisible(treePath);
		treeView.expandPath(treePath);
	}

	private void updateNoteToNode() {
		if (fromNode != null && (fromNode != treeModel.getRoot())) {
			if (fromNode.getUserObject().getClass() == OV_Task.class) {
				OV_Task task = (OV_Task) fromNode.getUserObject();
				String s = noteEditor.getText();
				{
					int pos = s.indexOf("\n");
					if (pos == -1) {
						task.subject = s;
						task.note = null;
					} else {
						task.subject = s.substring(0, pos);
						task.note = s.substring(pos + 1, s.length());
						if (task.note.length() == 0) {
							task.note = null;
						}
					}

				}
			}
		}
		if (fromNode != null && fromNode.getParent() != null) { // move , so isolated
			treeModel.reload(fromNode);
		}
	}

	public void changeSelection(DefaultMutableTreeNode fromNode, DefaultMutableTreeNode toNode) {

		valueChanged();
		updateNoteToNode();
		if (toNode != null && toNode != treeModel.getRoot()) {
			if (toNode.getUserObject().getClass() == OV_Task.class) {
				OV_Task task = (OV_Task) toNode.getUserObject();
				if (task.note == null) {
					task.note = new String("");
				}
				String s = task.subject + "\n" + task.note;
				noteEditor.setText(s);
			}
		} else {
			noteEditor.setText("Do not edit note for root");
		}
	}

	String space = new String("                            ");

	public void printItemSub(int level, String tag, DefaultMutableTreeNode node) {

		for (int i = 0; i < node.getChildCount(); i++) {
			String ctag = tag + (i + 1) + ".";
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			OV_Task task = (OV_Task) child.getUserObject();
			System.out.println(space.substring(0, level * 2) + ctag + " " + task.subject);
			printItemSub(level + 1, ctag, child);
		}
	}

	public void closeWindow() {
		int Response = JOptionPane.showConfirmDialog(null, "Will do you save data ? ");
		if (Response == JOptionPane.YES_OPTION) {
			// JOptionPane.showMessageDialog(null, "YEST");
			__save();
		}
	}



	public void expensionFrame() {
		if (openNote) {
			openNote = false;
			setFrame(400);
		} else {
			openNote = true;
			setFrame(900);
		}
	}

	public void setFrame(int width) {
		JFrame frame = (JFrame) treeView.getTopLevelAncestor();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) screenSize.getHeight() - 50;

		Dimension windowSize = new Dimension(width, height);
		frame.setSize(windowSize);
		frame.setLocation(screenSize.width - width, 0);

		// Dimension frameSize = this.getSize();
		// if (frameSize.height > screenSize.height) {
		// frameSize.height = screenSize.height;
		// }
		// if (frameSize.width > screenSize.width) {
		// frameSize.width = screenSize.width;
		// }

	}

//	public void setButtonMenu(TinyToolbar menubar) {
//		buttonMenu = menubar;
//	}

}
