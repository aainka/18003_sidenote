package com.elg.uis;

import java.util.LinkedList;

import platform.sidenote.OV_Task;

public class OV_TaskGroup extends LinkedList<OV_Task> {
	public String name;
	public String toString() {
		return name;
	}
}
