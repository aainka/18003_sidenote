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
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class SPTreeModel extends DefaultTreeModel implements TreeSelectionListener {

	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
	private List<OV_TaskGroup> slist = new LinkedList<OV_TaskGroup>();
	private String[] groupNames = new String[] { "회사업무", "점검사항", "취미", "플랫폼", "WoodCafe", "개인업무" };
	private List<OV_Task> list = null;
	private File file = new File("C:/tmp/aaa.gson");
	int seqNumber = 1;

	public SPTreeModel() {
		super(root);
		// init();
		// load();
		test_data();
	}

	public void init() {
		for (String gname : groupNames) {
			buildKind(gname);
		}
	}

	public void test_data() {
		DefaultMutableTreeNode aaaa = new DefaultMutableTreeNode("aaaa");
		aaaa.add(new DefaultMutableTreeNode("xxxx"));
		root.add(aaaa);
		root.add(new DefaultMutableTreeNode("dddd"));
		root.add(new DefaultMutableTreeNode("cccc"));
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

	private boolean find(String msg, String word) {
		if (msg != null && msg.indexOf(word) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Seletion Change
	 */





	// *************************************************
	// ***
	// *** File Load / Save
	// ***
	// *************************************************

	public void save() throws IOException {

		list = new LinkedList<OV_Task>();
		buildList((DefaultMutableTreeNode) this.getRoot());
		System.out.println("SAVE ::: count=" + list.size());

		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OutputStream outputStream = new FileOutputStream(file);
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
		gson.toJson(list, listType, bufferedWriter);
		bufferedWriter.close();
	}

	private void save2() {
		init();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(root, root.getClass());
		System.out.println(json);
	}

	private void buildList(DefaultMutableTreeNode node) {
		if (node.getChildCount() == 0) {
			return;
		}
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getFirstChild();
		while (child != null) {
			list.add((OV_Task) child.getUserObject());
			// todo add child index;
			buildList(child);
			child = child.getNextSibling();
		}
	}

	public DefaultMutableTreeNode createNode(DefaultMutableTreeNode parent, String pSubject) {

		OV_Task nTask = new OV_Task();
		nTask.subject = pSubject;
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

	public void delete() {

	}

	private void load() {
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
}
