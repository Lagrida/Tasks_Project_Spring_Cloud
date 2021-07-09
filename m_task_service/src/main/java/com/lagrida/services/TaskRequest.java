package com.lagrida.services;

import javax.validation.Valid;


import org.springframework.web.multipart.MultipartFile;

import com.lagrida.entities.Task;

public class TaskRequest {

	private MultipartFile[] files;
	
	@Valid
	private Task task;
	
	public TaskRequest() {
		
	}

	public Task getTask() {
		return task;
	}

	public TaskRequest(MultipartFile[] files, Task task) {
		this.files = files;
		this.task = task;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}
	
	/*public MultipartFile getFiles() {
		return files;
	}*/

	/*public TaskRequest(MultipartFile files, Task task) {
		this.files = files;
		this.task = task;
	}*/

}
