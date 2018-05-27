package platform.sidenote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import platform.sidenote.restapi.HostConfig;
import platform.sidenote.restapi.RestClient;

public class TaskTreeModel extends DataTreeModel {

	// private List<OV_Task> list = null;

	RestClient client = new RestClient();

	int seqNumber = 1;
	Type listType = new TypeToken<List<OV_Task>>() {
	}.getType();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	TaskTreeController taskTreeController;

	public TaskTreeModel(TaskTreeController taskTreeController) {
		this.taskTreeController = taskTreeController;
		List<OV_Task> list = loadNodes();
		taskTreeController.tableModel.buildData(list);
	}

	public static File getFile() {
		File file = null;
		if (HostConfig.isLinux()) {
			file = new File("/proj7/side_note/data/aaa_web.gnote");
		} else {
			file = new File("C:/tmp/aaa_web.gnote");
		}
		return file;
	}

	// ************************************************************
	// *** SAVE
	// ************************************************************

	public int saveFile(List<OV_Task> list) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();
		try {
			OutputStream outputStream = new FileOutputStream(getFile());
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			gson.toJson(list, listType, bufferedWriter);
			bufferedWriter.close();
			return list.size();
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static List<OV_Task> loadFile() throws FileNotFoundException, UnsupportedEncodingException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();

		FileInputStream fis = new FileInputStream(getFile());
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		JsonReader reader = new JsonReader(br);

		List<OV_Task> list = gson.fromJson(reader, listType);
		return list;
	}

	public List<OV_Task> webLoader() throws ClientProtocolException, IOException {
		List<OV_Task> list = client.list();
		return list;
	}

	private int webSave(List<OV_Task> list) {
		try {
			client.update(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.size();
	}

	@Override
	public int saveNodes() {
		List<OV_Task> list = new ArrayList<OV_Task>();
		buildAllNode(0, list, (DefaultMutableTreeNode) this.getRoot());
		int count;
		if (HostConfig.isWebMode()) {
			count = webSave(list);
		} else {
			count = saveFile(list);
		}
		return count;

	}

	public void buildAllNode(int parent_id, List<OV_Task> list, DefaultMutableTreeNode node) {
		if (node.getChildCount() == 0) {
			return;
		}
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getFirstChild();
		while (child != null) {
			OV_Task task = (OV_Task) child.getUserObject();
			task.parent_id = parent_id;
			list.add(task);
			buildAllNode(task.id, list, child);
			child = child.getNextSibling();
		}
	}
	
	

	// ************************************************************
	// *** LOAD
	// ************************************************************

	@Override
	public List<OV_Task> loadNodes() {
		System.out.println("LOAD DATA");
		seqNumber = 1;
		HashMap<Integer, DefaultMutableTreeNode> map = new HashMap<Integer, DefaultMutableTreeNode>();
		try {
			List<OV_Task> list = null;
			if (HostConfig.isWebMode()) {
				list = webLoader();
			} else {
				list = loadFile();
			}

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
			return list ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	// Clip Board Copy

	@Override
	public DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
		OV_Task task = (OV_Task) node.getUserObject();
		OV_Task nTask = task.copy();

		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode();
		node2.setUserObject("[" + encodeTreeNode(node) + "]"); // User Object change to String for TreeInfo;
		// node2.setUserObject(nTask);
		return node2;
	}

	public String encodeTreeNode(DefaultMutableTreeNode node) {
		OV_Task task = (OV_Task) node.getUserObject();
		String s = gson.toJson(task, OV_Task.class);
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			System.out.println("encode count=" + node.getChildCount());
			s += "," + encodeTreeNode(child);
		}
		return new String(s);
	}

	public DefaultMutableTreeNode decodeTreeNode(String jString) {
		HashMap<Integer, DefaultMutableTreeNode> map = new HashMap<Integer, DefaultMutableTreeNode>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();
		List<OV_Task> list = gson.fromJson(jString, listType);
		System.out.println("decodeNode. =" + list.size());
		OV_Task task = list.get(0);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(list.get(0));
		if (task.id > 0) {
			map.put(task.id, top);
		}
		for (int i = 1; i < list.size(); i++) {
			task = list.get(i);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(task);
			if (task.id > 0) {
				map.put(task.id, node);
			}
			if (task.parent_id > 0) {
				DefaultMutableTreeNode pNode = map.get(task.parent_id);
				if (pNode != null) {
					pNode.add(node);
					continue;
				}
			}
			top.add(node);
		}
		return top;
	}
}
