package com.lagrida.templates;

public class TaskFile {
	private long id;
	private String fileName;
	private String fileOriginalName;
	private String fileDirectory;
	private String fileExtension;
	private long fileSize;
	
	public TaskFile() {

	}
	public TaskFile(String fileName, String fileOriginalName, String fileDirectory, String fileExtension, long fileSize) {
		this.fileName = fileName;
		this.fileOriginalName = fileOriginalName;
		this.fileDirectory = fileDirectory;
		this.fileExtension = fileExtension;
		this.fileSize = fileSize;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileOriginalName() {
		return fileOriginalName;
	}
	public void setFileOriginalName(String fileOriginalName) {
		this.fileOriginalName = fileOriginalName;
	}
	public String getFileDirectory() {
		return fileDirectory;
	}
	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
