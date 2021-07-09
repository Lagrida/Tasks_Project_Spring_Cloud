package com.lagrida.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lagrida.entities.Task;

public class TaskEager {
	
	private Task task;
	private List<User> users = new ArrayList<User>();
	private List<TaskFile> files = new ArrayList<TaskFile>();
	
	public TaskEager() {}
	
	public TaskEager(Task task, List<User> users, List<TaskFile> files) {
		this.task = task;
		this.users = users;
		this.files = files;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<TaskFile> getFiles() {
		return files;
	}
	public void setFiles(List<TaskFile> files) {
		this.files = files;
	}
}
