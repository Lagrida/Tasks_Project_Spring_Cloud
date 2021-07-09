package com.lagrida.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = {
	@Index(name = "file_name_index", columnList = "fileName"),
	@Index(name = "service_source", columnList = "serviceSource")
})
public class AppFile {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String fileName;
	private String fileOriginalName;
	private String fileDirectory;
	private String fileExtension;
	private long fileSize;
	
	private long userOwner;
	
	private String serviceSource;
	
	private long source;
	
	private String additionalSource;
	
	public AppFile() {}
	
	public AppFile(String fileName, String fileOriginalName, String fileDirectory, String fileExtension, long userOwner, String serviceSource, long source, String additionalSource) {
		this.fileName = fileName;
		this.fileOriginalName = fileOriginalName;
		this.fileDirectory = fileDirectory;
		this.fileExtension = fileExtension;
		this.userOwner = userOwner;
		this.serviceSource = serviceSource;
		this.source = source;
		this.additionalSource = additionalSource;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFileName(){
		return fileName;
	}
	public void setFileName(String fileName){
		this.fileName = fileName;
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
	public long getUserOwner() {
		return userOwner;
	}
	public void setUserOwner(long userOwner) {
		this.userOwner = userOwner;
	}
	public String getFileOriginalName() {
		return fileOriginalName;
	}
	public void setFileOriginalName(String fileOriginalName) {
		this.fileOriginalName = fileOriginalName;
	}
	public String getServiceSource() {
		return serviceSource;
	}
	public void setServiceSource(String serviceSource) {
		this.serviceSource = serviceSource;
	}
	public long getSource() {
		return source;
	}
	public void setSource(long source) {
		this.source = source;
	}
	public String getAdditionalSource() {
		return additionalSource;
	}
	public void setAdditionalSource(String additionalSource) {
		this.additionalSource = additionalSource;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
