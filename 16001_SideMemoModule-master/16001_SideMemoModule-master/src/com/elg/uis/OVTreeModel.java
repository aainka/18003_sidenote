package com.elg.uis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import platform.sidenote.DataTreeModel;
import platform.sidenote.OV_Task;
import platform.sidenote.SPTree;

public class OVTreeModel extends DataTreeModel {

	private List<OV_TaskGroup> slist = new LinkedList<OV_TaskGroup>();
	private String[] groupNames = new String[] { "회사업무", "점검사항", "취미", "플랫폼", "WoodCafe", "개인업무" };
 
	public OVTreeModel() {
		// init();
		// load();
	}

	public void init() {
		for (String gname : groupNames) {
			buildKind(gname);
		}
	}

	public void buildKind(String category) {
		OV_TaskGroup taskGroup = new OV_TaskGroup();
		DefaultMutableTreeNode firstNode = new DefaultMutableTreeNode(taskGroup);
		root.add(firstNode);
		taskGroup.name = category;
		for (OV_Task task : list) {
			if (find(task.category, category)) {
				DefaultMutableTreeNode secondNode = new DefaultMutableTreeNode(task);
				firstNode.add(secondNode);
				taskGroup.add(task);
			}
		}
		System.out.println("Category " + category + ", cout=" + taskGroup.size());
		slist.add(taskGroup);
	}

  

	DefaultMutableTreeNode fromNode = null;

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		SPTree tree = (SPTree) e.getSource();
		DefaultMutableTreeNode toNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		tree.selectionChange(fromNode, toNode);
		fromNode = toNode;
	}

	 

	private void save2() {
		init();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(root, root.getClass());
		System.out.println(json);
	}

	@Override
	public DefaultMutableTreeNode createNode(DefaultMutableTreeNode parent, Object value) {
		OV_Task nTask = new OV_Task();
		nTask.subject = (String) value;
		nTask.id = seqNumber++;
		// time
		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(nTask);
		if (parent == getRoot()) {
			nTask.parent_id = 0;
		} else {
			OV_Task pTask = (OV_Task) parent.getUserObject();
			nTask.parent_id = pTask.id;
		}
		parent.insert(newChild, parent.getChildCount());
		return newChild;
	}

	@Override
	public void saveNodes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadNodes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
