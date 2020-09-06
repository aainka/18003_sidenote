package com.platform.note3;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SPTree extends JTree {

	// 말풍선 만들기
	// 액셀로 재저장
	
	DataTreeModel treeModel = null;

	public SPTree(DataTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public void init() {

		this.setModel(treeModel);
		this.setAutoscrolls(true);
		this.setRootVisible(true);

		this.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		this.addTreeSelectionListener(treeModel);

		this.setDragEnabled(true);
		this.setDropMode(DropMode.ON_OR_INSERT);
		this.setTransferHandler(new TreeTransferHandler99());

		expandTree(this);
	}

	private void expandTree(JTree tree) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		Enumeration e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			if (node.isLeaf())
				continue;
			int row = tree.getRowForPath(new TreePath(node.getPath()));
			tree.expandRow(row);
		}
	}

	public void quickCreateNode(String pSubject) {
		if (pSubject.indexOf("save") == 0) {
			treeModel.saveNodes();
			return;
		}
		if (pSubject.indexOf("delete") == 0) {
			// treeModel.removeNodeFromParent(node);
			return;
		}
		// new creation
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
		if (parent == null) {
			parent = (DefaultMutableTreeNode) this.getModel().getRoot();
		} else {
			System.out.println("add.parent=" + parent);
		}
		DefaultMutableTreeNode newChild = treeModel.createNode(parent, pSubject);

		this.updateUI();
		TreePath treePath = new TreePath(newChild.getPath());
		scrollPathToVisible(treePath);
		expandPath(treePath);
	}

	private JEditorPane editPane = new JEditorPane();

	public JEditorPane getEditPane() {
		// TODO Auto-generated method stub
		return editPane;
	}

	public void selectionChange(DefaultMutableTreeNode fromNode, DefaultMutableTreeNode toNode) {
		System.out.println("selectionChanged");
		OV_Task task = null;
		if (fromNode != null && (fromNode != treeModel.getRoot())) {
			if (fromNode.getUserObject().getClass() == OV_Task.class) {
				task = (OV_Task) fromNode.getUserObject();
				task.note = editPane.getText();
			}
		}
		if (toNode != null && toNode != treeModel.getRoot()) {
			if (toNode.getUserObject().getClass() == OV_Task.class) {
				task = (OV_Task) toNode.getUserObject();
				editPane.setText(task.note);
			}
		} else {
			editPane.setText("Do not edit note for root");
		}
	}
}

class TreeTransferHandler99 extends TransferHandler {
	DataFlavor nodesFlavor;
	DataFlavor[] flavors = new DataFlavor[1];
	DefaultMutableTreeNode[] nodesToRemove;

	public TreeTransferHandler99() {
		try {
			String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\""
					+ javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
			nodesFlavor = new DataFlavor(mimeType);
			flavors[0] = nodesFlavor;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound: " + e.getMessage());
		}
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}

	@Override
	// Drag를 시작하면 이동할 객체를 만든다. JTree의 Point를 넘겨받는다. #1
	protected Transferable createTransferable(JComponent c) {

		JTree tree = (JTree) c;
		DataTreeModel treeModel = (DataTreeModel) tree.getModel();
		TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			// Make up a node array of copies for transfer and
			// another for/of the nodes that will be removed in
			// exportDone after a successful drop.
			List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
			List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
			DefaultMutableTreeNode copy = treeModel.deepCopy(node);
			copies.add(copy);
			toRemove.add(node);
			for (int i = 1; i < paths.length; i++) {
				DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				// Do not allow higher level nodes to be added to list.
				if (next.getLevel() < node.getLevel()) {
					break;
				} else if (next.getLevel() > node.getLevel()) { // child node
					copy.add(treeModel.deepCopy(next));
					// node already contains child
				} else { // sibling
					copies.add(treeModel.copy(next));
					toRemove.add(next);
				}
			}
			System.out.println(" + copies1 = " + copies.get(0));
			DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
			System.out.println("[1======] createTransferable() nodes.count=" + copies.size());
			for (DefaultMutableTreeNode n : copies) {
				OV_Task task = (OV_Task) nodes[0].getUserObject();
				System.out.println(" + copies = " + task.subject);
			}

			nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
			return new NodesTransfer22(nodes);
		}
		return null;
	}

	/** Defensive copy used in createTransferable. */
	private DefaultMutableTreeNode copy99(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(node);
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("king");
		node1.add(node2);
		return node1;
	}

	// only check drop location
	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}
		support.setShowDropLocation(true);
		// if (!support.isDataFlavorSupported(nodesFlavor)) {
		// return false;
		// }
		// Do not allow a drop on the drag source selections.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		JTree tree = (JTree) support.getComponent();
		int dropRow = tree.getRowForPath(dl.getPath());
		int[] selRows = tree.getSelectionRows();
		for (int i = 0; i < selRows.length; i++) {
			if (selRows[i] == dropRow) {
				return false;
			}
		}
		return true;
	}

	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}
		System.out.println("1) canImport_OK");
		// Extract transfer data.
		DefaultMutableTreeNode[] nodes = null;
		try {
			Transferable t = support.getTransferable();
			nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
		} catch (UnsupportedFlavorException ufe) {
			System.out.println("UnsupportedFlavor: " + ufe.getMessage());
		} catch (java.io.IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
		}
		// Get drop location info.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		int childIndex = dl.getChildIndex();
		TreePath dest = dl.getPath();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
		JTree tree = (JTree) support.getComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		{
			System.out.println("2) 0 importData.count=" + nodes.length + "to parent=" + parent);
			for (int i = 0; i < nodes.length; i++) {
				System.out.println("  + node = " + nodes[i].getUserObject());
			}
		}
		// Configure for drop mode.
		int index = childIndex; // DropMode.INSERT
		System.out.println("2) importData.--1");
		if (childIndex == -1) { // DropMode.ON
			index = parent.getChildCount();
		}
		// Add data to model.
		for (int i = 0; i < nodes.length; i++) {
			model.insertNodeInto(nodes[i], parent, index++);
		}
		return true;
	}

	protected void exportDone(JComponent source, Transferable data, int action) {
		System.out.println("[1======] exportDone action=" + action);
		if ((action & MOVE) == MOVE) {
			JTree tree = (JTree) source;
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Remove nodes saved in nodesToRemove in createTransferable.
			for (int i = 0; i < nodesToRemove.length; i++) {
				model.removeNodeFromParent(nodesToRemove[i]);
			}
		}
	}

	public class NodesTransfer22 implements Transferable {
		DefaultMutableTreeNode[] nodes;

		public NodesTransfer22(DefaultMutableTreeNode[] nodes) {
			this.nodes = nodes;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return nodes;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return nodesFlavor.equals(flavor);
		}
	}
}
