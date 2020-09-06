package com.platform.note3;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public abstract class DataTreeModel extends DefaultTreeModel implements TreeSelectionListener {

	protected static DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

	public abstract void saveNodes();
	public abstract void loadNodes();
	public abstract DefaultMutableTreeNode createNode(DefaultMutableTreeNode parent, Object value);
	public abstract DefaultMutableTreeNode copy(DefaultMutableTreeNode node) ;
 

	public DataTreeModel() {
		super(root);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void copyChildren(DefaultMutableTreeNode sNode, DefaultMutableTreeNode dNode) {
		if (sNode.getChildCount() == 0) {
			return;
		}
		for (int i = 0; i < sNode.getChildCount(); i++) {
			DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) sNode.getChildAt(i);
			dNode.add(copy(cNode));
			copyChildren(cNode, dNode);
		}
	}
	
 
	public DefaultMutableTreeNode deepCopy(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode sNode = copy(node);
		copyChildren(node, sNode);
		return sNode;
	}
}
