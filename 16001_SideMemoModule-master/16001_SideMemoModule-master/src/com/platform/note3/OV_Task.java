package com.platform.note3;

import java.util.Date;

import javax.swing.tree.DefaultMutableTreeNode;

public class OV_Task {
	int id;
	String subject;
	Date created;
	Date started;
	Date closed;
	String requestedby;
	String assigned;
	int parent_id;
	public String category;
	public String priority;
	public String note;
	
	public OV_Task copy() {
		OV_Task n = new OV_Task();
		n.subject = this.subject;
		n.id = this.id;
		n.parent_id = this.parent_id;
		n.note = this.note;
		return n;
	}
	
	public String toString() {
		return subject;
	}
}
