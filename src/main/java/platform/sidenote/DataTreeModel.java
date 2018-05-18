package platform.sidenote;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public abstract class DataTreeModel extends DefaultTreeModel {

	protected static DefaultMutableTreeNode root = new DefaultMutableTreeNode("SYSTEM");

	public abstract int saveNodes();

	public abstract int loadNodes();

	public abstract DefaultMutableTreeNode createNode(DefaultMutableTreeNode parent, Object value);

	public abstract DefaultMutableTreeNode copy(DefaultMutableTreeNode node);

	public abstract DefaultMutableTreeNode decodeTreeNode(String userObject);

	public DataTreeModel() {
		super(root);
		// TODO Auto-generated constructor stub
	}

	// public void copyChildren(DefaultMutableTreeNode sNode, DefaultMutableTreeNode
	// dNode) {
	// if (sNode.getChildCount() == 0) {
	// return;
	// }
	// for (int i = 0; i < sNode.getChildCount(); i++) {
	// DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) sNode.getChildAt(i);
	// dNode.add(copy(cNode));
	// copyChildren(cNode, dNode);
	// }
	// }
	//
	//
	// public DefaultMutableTreeNode deepCopy(DefaultMutableTreeNode node) {
	// DefaultMutableTreeNode sNode = copy(node);
	// copyChildren(node, sNode);
	// return sNode;
	// }
}
