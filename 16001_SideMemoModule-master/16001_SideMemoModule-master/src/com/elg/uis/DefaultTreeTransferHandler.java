package com.elg.uis;

import java.awt.Point;
import java.awt.dnd.DnDConstants;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * 
* <pre>
* <p> Title: DefaultTreeTransferHandler.java </p>
* <p> Description: Drag/Drap Transfer HandlerŬ����</p>
* </pre>
*
* @author Cheol Jong Park
* @created: 2015.06
* @modified:
*
 */
public class DefaultTreeTransferHandler extends AbstractTreeTransferHandler {
	
	/**
	 * ������
	 * @param tree JTree ��ü
	 * @param action drag/drop action Mode
	 */
	public DefaultTreeTransferHandler(DNDTree tree, int action) {
		super(tree, action, true);
		
	}
	
	@Override
	public boolean canPerformAction(DNDTree tree, DefaultMutableTreeNode draggedNode, int action, Point location) {
		TreePath pathTarget = tree.getPathForLocation(location.x, location.y);
		if (pathTarget == null) {
			tree.setSelectionPath(null);
			return(false);
			
		} 
		tree.setSelectionPath(pathTarget);

		if(action == DnDConstants.ACTION_COPY) {
			return(true);
		}
		else if(action == DnDConstants.ACTION_MOVE) 
		{     
			DefaultMutableTreeNode targetDefaultMutableTreeNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();    
			TreeNode targetTreeNode = (TreeNode)targetDefaultMutableTreeNode.getUserObject();
			
			if (draggedNode.isRoot() || 
				targetDefaultMutableTreeNode == draggedNode.getParent() || 
				draggedNode.isNodeDescendant(targetDefaultMutableTreeNode)) {                         
				return(false);     
			}
			else 
			{
				DefaultMutableTreeNode parentDefaultMutableTreeNode = (DefaultMutableTreeNode)draggedNode.getParent();
				TreeNode dragParentTreeNode = (TreeNode)parentDefaultMutableTreeNode.getUserObject();
				
				// source ����� �θ� "����:"�� ��� false
				if(dragParentTreeNode.getName().startsWith(DNDTree.FILTER_NAME)) {
					return (false);
				}
				// dest ��尡 ���Ϳ� ������ �ڽ��ΰ��  false
				else
				{
					if(targetTreeNode.getName().startsWith(DNDTree.FILTER_NAME)) {
						return (true);
					}
					else
					{
						if(targetDefaultMutableTreeNode.isRoot())
						{
							return(true);
						}
						else
						{
							DefaultMutableTreeNode targetParentDefaultMutableTreeNode = (DefaultMutableTreeNode)targetDefaultMutableTreeNode.getParent();
							TreeNode targetParentTreeNode = (TreeNode)targetParentDefaultMutableTreeNode.getUserObject();

							if(targetParentTreeNode.getName().startsWith(DNDTree.FILTER_NAME)) {
								return (false);
							}
						}
					}
					
				}

				return(true);
			}                     
		}
		else {          
			return(false);     
		}
	}
	
	@Override
	public boolean executeDrop(DNDTree target, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode newParentNode, int action) { 
		if (action == DnDConstants.ACTION_COPY) {
			DefaultMutableTreeNode newNode = target.makeDeepCopy(draggedNode);
			
			((DefaultTreeModel)target.getModel()).insertNodeInto(newNode,newParentNode,newParentNode.getChildCount());
			return(true);
		}
		if (action == DnDConstants.ACTION_MOVE) {
			
			
			String name = newParentNode.toString();
			if(name.startsWith(DNDTree.FILTER_NAME))
			{
				String filter = name.replace(DNDTree.FILTER_NAME, "");
				
				TreeNode treeNode = (TreeNode)draggedNode.getUserObject();
				String nameTmp = treeNode.getName();
				String desc = treeNode.getDesc();
				if(!desc.contains(filter))
				{
					desc += " " + filter;
					tree.updateTreeNode(draggedNode, nameTmp, desc);
				}
	
			}
			else
			{
				draggedNode.removeFromParent();
				((DefaultTreeModel)target.getModel()).insertNodeInto(draggedNode,newParentNode,newParentNode.getChildCount());

				TreePath treePath = new TreePath(draggedNode.getPath());
				tree.scrollPathToVisible(treePath);
				tree.setSelectionPath(treePath);

				return(true);
			}
		}
		return(false);
	}
	

}