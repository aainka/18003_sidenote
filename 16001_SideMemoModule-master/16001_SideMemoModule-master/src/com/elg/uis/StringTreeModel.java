package com.elg.uis;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.platform.note3.DataTreeModel;
import com.platform.note3.OV_Task;

@SuppressWarnings("serial")
public class StringTreeModel extends DataTreeModel {

	public StringTreeModel() {
		loadNodes();
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveNodes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadNodes() {

		DefaultMutableTreeNode aaa = new DefaultMutableTreeNode("aaa");
		this.insertNodeInto(aaa, root, root.getChildCount());
		DefaultMutableTreeNode bbb = new DefaultMutableTreeNode("bbb");
		this.insertNodeInto(bbb, root, root.getChildCount());
		DefaultMutableTreeNode ccc = new DefaultMutableTreeNode("ccc");
		this.insertNodeInto(ccc, root, root.getChildCount());

		DefaultMutableTreeNode a1 = new DefaultMutableTreeNode("a1");
		this.insertNodeInto(a1, aaa, aaa.getChildCount());
		DefaultMutableTreeNode a2 = new DefaultMutableTreeNode("a2");
		this.insertNodeInto(a2, aaa, aaa.getChildCount());
	}

	@Override
	public DefaultMutableTreeNode createNode(DefaultMutableTreeNode parent, Object value) {
		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(value);
		insertNodeInto(newChild, parent, parent.getChildCount());
		return newChild;
	}

	public DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
		OV_Task task = (OV_Task) node.getUserObject();
		OV_Task nTask = task.copy();
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode();
		node2.setUserObject(nTask);
		return node2;
	}


	

}
