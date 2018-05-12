package platform.sidenote;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SPTree extends JTree {

	private Debug logger = Debug.getLogger(this.getClass());

	public SPTree(DataTreeModel treeModel) {
		this.setModel(treeModel);
		 this.setCellRenderer(new TaskTreeCellRenderer());
		init();
	}

	public void init() {
		this.setAutoscrolls(true);
		this.setRootVisible(true);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		this.setDragEnabled(true);

		this.setDropMode(DropMode.ON_OR_INSERT);
		this.setTransferHandler(new TreeTransferHandler99());
		expandTree(this);
		OT_Popup pp = new OT_Popup(this);
		pp.addMethodCall("Collapse", this, "collapseAll");
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

	public void collapseAll(SPTree tree) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		TreePath treePath = tree.getSelectionPath();
		if (treePath == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		Enumeration e = node.children();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) e.nextElement();
			TreeNode[] a = child.getPath();
			TreePath path = new TreePath(child.getPath());
			tree.collapsePath(path);
		}
	}

}

class TreeTransferHandler99 extends TransferHandler {
	DataFlavor nodesFlavor;
	DataFlavor[] flavors = new DataFlavor[1];
	DefaultMutableTreeNode[] nodesToRemove;
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
	// Drag�� �����ϸ� �̵��� ��ü�� �����. JTree�� Point�� �Ѱܹ޴´�. #1
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
			DefaultMutableTreeNode copy = treeModel.copy(node);

			copies.add(copy);
			toRemove.add(node);
			for (int i = 1; i < paths.length; i++) {
				DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				// Do not allow higher level nodes to be added to list.
				if (next.getLevel() < node.getLevel()) {
					break;
				} else if (next.getLevel() > node.getLevel()) { // child node
					copy.add(treeModel.copy(next));
					// node already contains child
				} else { // sibling
					copies.add(treeModel.copy(next));
					toRemove.add(next);
				}
			}

			DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);

			nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
			return new NodesTransfer22(nodes); // ----->
		}
		return null;
	}

	// only check drop location
	public boolean canImport(TransferHandler.TransferSupport support) {
		System.out.println("camImport-0");
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
		System.out.println("import----" + support);
		Transferable t2 = support.getTransferable();
		try {
			System.out.println("getUserDropAction::----" + t2.getTransferData(nodesFlavor));
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!canImport(support)) {
			return false;
		}

		// Extract transfer data.
		DefaultMutableTreeNode[] nodes = null;
		try {
			Transferable t = support.getTransferable();
			nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor); // <-----------
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
		DataTreeModel treeModel = (DataTreeModel) tree.getModel();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		// Configure for drop mode.
		int index = childIndex; // DropMode.INSERT
		System.out.println("2) importData.--1");
		if (childIndex == -1) { // DropMode.ON
			index = parent.getChildCount();
		}
		// Add data to model.
		for (int i = 0; i < nodes.length; i++) {
			// model.insertNodeInto(nodes[i], parent, index++);
			model.insertNodeInto(treeModel.decodeTreeNode((String) nodes[i].getUserObject()), parent, index++);
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
			System.out.println("make NodeTransfer");
			;
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
