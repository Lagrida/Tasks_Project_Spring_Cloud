package com.lagrida.templates;

import java.util.HashSet;
import java.util.Set;

public class Task {
	
	private long id;
	private long userOwner;
	private Set<String> files = new HashSet<String>();
	
	public Task() {
		
	}
	public Task(long id, long userOwner, Set<String> files) {
		this.id = id;
		this.userOwner = userOwner;
		this.files = files;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserOwner() {
		return userOwner;
	}
	public void setUserOwner(long userOwner) {
		this.userOwner = userOwner;
	}
	public Set<String> getFiles() {
		return files;
	}
	public void setFiles(Set<String> files) {
		this.files = files;
	}
}
