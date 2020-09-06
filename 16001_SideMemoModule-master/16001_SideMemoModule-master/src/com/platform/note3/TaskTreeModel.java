package com.platform.note3;

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
import javax.swing.tree.DefaultMutableTreeNode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class TaskTreeModel extends DataTreeModel {

	private List<OV_Task> list = null;
	private File file = new File("C:/tmp/aaa.gson");
	int seqNumber = 1;

	public TaskTreeModel() {
		loadNodes();
	}

	@Override
	public void saveNodes() {
		list = new LinkedList<OV_Task>();
		buildSaveList(0,(DefaultMutableTreeNode) this.getRoot());
		System.out.println("SAVE ::: count=" + list.size());

		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			OutputStream outputStream = new FileOutputStream(file);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			gson.toJson(list, listType, bufferedWriter);
			bufferedWriter.close();
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildSaveList(int parent_id, DefaultMutableTreeNode node) {
		if (node.getChildCount() == 0) {
			return;
		}
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getFirstChild();
		while (child != null) {
			OV_Task task = (OV_Task) child.getUserObject();
			task.parent_id = parent_id;
			list.add(task);
			buildSaveList(task.id, child);
			child = child.getNextSibling();
		}
	}

	@Override
	public void loadNodes() {
		seqNumber = 1;
		HashMap<Integer, DefaultMutableTreeNode> map = new HashMap<Integer, DefaultMutableTreeNode>();
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Type listType = new TypeToken<List<OV_Task>>() {
			}.getType();

			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			JsonReader reader = new JsonReader(br);

			List<OV_Task> list = gson.fromJson(reader, listType);
			for (OV_Task task : list) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode();
				node.setUserObject(task);

				DefaultMutableTreeNode pNode = null;
				if (task.parent_id != 0) {
					pNode = map.get(task.parent_id);
				}
				if (pNode == null) {
					root.add(node);
				} else {
					pNode.add(node);
				}
				if (task.id != 0) {
					map.put(task.id, node);
				}
				if (task.id >= seqNumber) {
					seqNumber = task.id + 1;
				}
			}
			for (OV_Task task : list) {
				if (task.id == 0) {
					task.id = seqNumber++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// List<String> target2 = gson.fromJson(json, listType);

		// System.out.println("Count=" + list.size());
		// String aa = gson.toJson(list, listType);
		// System.out.println(aa);

	}

	@Override
	public DefaultMutableTreeNode createNode(DefaultMutableTreeNode parent, Object value) {
		OV_Task task = new OV_Task();
		task.id = seqNumber++;
		task.subject = (String) value;
		if (parent.getUserObject() instanceof OV_Task) {
			OV_Task pTask = (OV_Task) parent.getUserObject();
			task.parent_id = pTask.id;
		}
		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(task);
		parent.add(newChild);
		return newChild;
	}

	@Override
	public DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
		OV_Task task = (OV_Task) node.getUserObject();
		OV_Task nTask = task.copy();
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode();
		node2.setUserObject(nTask);
		return node2;
	}

	DefaultMutableTreeNode fromNode = null;
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		SPTree tree = (SPTree) e.getSource();
		DefaultMutableTreeNode toNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		tree.selectionChange(fromNode, toNode);
		fromNode = toNode;
	}

}
