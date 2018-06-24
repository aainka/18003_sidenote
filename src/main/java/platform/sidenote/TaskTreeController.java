package platform.sidenote;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import platform.sidenote.util.TinyValue;

public class TaskTreeController implements TreeSelectionListener {

	TaskTableModel tableModel = new TaskTableModel(this);
	JTable table = new JTable(tableModel);
	private TaskTreeModel treeModel = new TaskTreeModel(this);
 	private SPTree treeView = new SPTree(treeModel);
	private JTextField textNew = new JTextField();
	private DefaultMutableTreeNode fromNode = null;
	private SPFrame frame;
	// Debug logger = Debug.getLogger(this.getClass());
  	private SPNoteEditor editor = new SPNoteEditor();

	TinyToolbar menubar = new TinyToolbar() {
		@Override
		public void init() {
			String cmds = "(SAVE,save),(PRINT,print),(Edit,editMode)";
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

		public void _editMode(Object source, Point p) {
			toggleFrameMode();
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
			String cmds = "(Collapse,collapse),(Priority,priority),(Title,title)";
			List<String[]> valueList = TinyValue.parse(cmds);
			addMenu(valueList);
			l = new TinyCallBackActionListener(this, OT_Popup.class, valueList);
			source = treeView;
			treeView.setComponentPopupMenu(this);
		}

		public void _collapse(Object source, Point poped) {
			DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
			// TreePath treePath = tree.getSelectionPath();
			TreePath treePath = treeView.getClosestPathForLocation(poped.x, poped.y);

			if (treePath == null) {
				return;
			}
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

			Enumeration e = node.children();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) e.nextElement();
				TreeNode[] a = child.getPath();
				TreePath path = new TreePath(child.getPath());
				treeView.collapsePath(path);
			}
		}

		public void _priority(Object source, Point poped) {
			OV_Task task = treeView.getTaskAt(poped);
			if (task.priority > 0) {
				task.priority = 0;
			} else {
				task.priority = 1;
			}
			treeView.updateUI();
			updateTableModel();
		}

		public void _title(Object source, Point poped) {
			OV_Task task = treeView.getTaskAt(poped);
			task.priority = 6;
			treeView.updateUI();
		}
	};

	public TaskTreeController() {
		// Document blank = new DefaultStyledDocument();
		// noteEditor.setDocument(blank);
	}

	public TinyToolbar getMenubar() {
		return menubar;
	}

	public void __save() {
		updateNoteToNode();
		int count = treeModel.saveNodes();
		JOptionPane.showMessageDialog(null, "" + count + " recodes are saved");
	}

	public void updateTableModel() {
		List<OV_Task> list = new ArrayList<OV_Task>();
		treeModel.buildAllNode(0, list, (DefaultMutableTreeNode) treeModel.getRoot());
		tableModel.buildData(list);
		table.updateUI();
	}

	// **************************************************************
	// *** Build User Interface
	// **************************************************************

	public JPanel buildUi() {

	

		/**
		 * New Input
		 */
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

		/**
		 * TreeView
		 */
		//  treeView.setMinimumSize(new Dimension(400, 100));
		 // treeView.setPreferredSize(new Dimension(400, 100));
		JScrollPane scTreeView = new JScrollPane(treeView);
		scTreeView.setPreferredSize(new Dimension(400, 250));

		/**
		 * TodoView
		 */
		JScrollPane scTable = new JScrollPane(table);
		scTable.setPreferredSize(new Dimension(400, 250));

		/**
		 * NoteEditor
		 */
		editor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				valueChanged();
				// coloring();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					updateNoteToNode();

				}
			}
		});
		JScrollPane scNoteEditor = new JScrollPane(editor);

		treeView.addTreeSelectionListener(this);
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		container.add(BorderLayout.NORTH, textNew);
		{

			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(200, 100));
			pan.setLayout(new BorderLayout());
			pan.add(BorderLayout.CENTER, scTreeView);
			pan.add(BorderLayout.SOUTH, scTable);
			
			scNoteEditor.setMinimumSize( new Dimension(1, 100));
			 

			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pan, scNoteEditor);
			splitPane.setPreferredSize(new Dimension(400, 100));
			System.out.println("s3="+splitPane.getPreferredSize());
			//splitPane.setContinuousLayout(true);
			//splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(500);
			splitPane.setDividerSize(10);
			//splitPane.setPreferredSize(new Dimension(600, 100));
			container.add(BorderLayout.CENTER, splitPane);
		}

		return container;
	}
	private void updateNoteToNode() {
		if (fromNode != null && (fromNode != treeModel.getRoot())) {
			if (fromNode.getUserObject().getClass() == OV_Task.class) {
				OV_Task task = (OV_Task) fromNode.getUserObject();
				String s = editor.getText();
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
				editor.setText(s);
			}
		} else {
			editor.setText("Do not edit note for root");
		}
	}
	// **********************************
	// *** FRAME
	// **********************************

	boolean isShowFrameMode = true;

	private void toggleFrameMode() {
		if (isShowFrameMode) {
			isShowFrameMode = false;
			editFrameMode();
		} else {
			isShowFrameMode = true;
			showFrameMode();
		}
	}

	private void editFrameMode() {
		Dimension size = frame.getSize();
		frame.setSize(900, size.height);
		frame.alignCentor();
	}

	private void showFrameMode() {
		 
		Dimension size = frame.getSize();
		frame.setSize(350, size.height);
		frame.alignRight();
	}

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

	public void setFrame(SPFrame spFrame) {
		this.frame = spFrame;

	}

	// public void expensionFrame() {
	// if (openNote) {
	// openNote = false;
	// setFrame(400);
	// } else {
	// openNote = true;
	// setFrame(900);
	// }
	// }

	// public void setFrame(int width) {
	// JFrame frame = (JFrame) treeView.getTopLevelAncestor();
	// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// int height = (int) screenSize.getHeight() - 50;
	//
	// Dimension windowSize = new Dimension(width, height);
	// frame.setSize(windowSize);
	// frame.setLocation(screenSize.width - width, 0);
	//
	// // Dimension frameSize = this.getSize();
	// // if (frameSize.height > screenSize.height) {
	// // frameSize.height = screenSize.height;
	// // }
	// // if (frameSize.width > screenSize.width) {
	// // frameSize.width = screenSize.width;
	// // }
	//
	// }

	// public void setButtonMenu(TinyToolbar menubar) {
	// buttonMenu = menubar;
	// }

}
