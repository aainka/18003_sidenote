package platform.sidenote;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class OV_Task {
	int id;
	public String subject;
	Date created;
	Date started;
	Date closed;
	String requestedby;
	String assigned;
	int parent_id;
	public String category;
	public String priority;
	public String note;

	public OV_Task(String string) {
		long mtime= System.currentTimeMillis();
		this.created = new Date(mtime);
	}

	public OV_Task() {
		long mtime= System.currentTimeMillis();
		this.created = new Date(mtime);
	}

	public OV_Task copy() {
		OV_Task n = new OV_Task("xx");
		n.subject = this.subject;
		n.id = this.id;
		n.parent_id = this.parent_id;
		n.note = this.note;
		return n;
	}

	public String toString() {
		return subject;
	}

	public static List<OV_Task> decode(String s) {
		// List<OV_Task> list = new ArrayList<OV_Task>();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<OV_Task> list = gson.fromJson(s, listType);
		return list;
	}

	public static String encodeList(List<OV_Task> tasks) {
		StringWriter writer = new StringWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_Task>>() {
		}.getType();
		gson.toJson(tasks, listType, writer);
		return writer.toString();
	}

	public String dump() {
		String s =
		 "id: "+id +
		 "\nsubject: "+subject ;
		return s;
		
	}
}
