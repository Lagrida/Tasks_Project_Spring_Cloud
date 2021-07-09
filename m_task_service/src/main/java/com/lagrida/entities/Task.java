package com.lagrida.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.JoinColumn;

import com.lagrida.templates.User;

@Entity
public class Task {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length=35)
	@NotNull(message = "title may not be null")
	@Size(min=2, max=35, message="title must have 4 to 35 caracteres")
	private String title;
	
	@Column(length=255)
	private String description;
	
	@ElementCollection(targetClass = Long.class)
	@CollectionTable
	private Set<Long> users = new HashSet<Long>();

	private long userOwner;
	
	@ElementCollection(targetClass = String.class)
	@CollectionTable
	private Set<String> files = new HashSet<String>();
	
	private int type = 0; // 0 task, 1 In progress, 2 completed
	
	@ColumnDefault(value="CURRENT_TIMESTAMP")
	@CreationTimestamp
	private LocalDateTime createdOn;
	
	@UpdateTimestamp
	private LocalDateTime updatedOn;
	
	public Task(){
		
	}

	public Task(
		String title,
		String description, Set<Long> users) {
		this.title = title;
		this.description = description;
		this.users = users;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Long> getUsers() {
		return users;
	}

	public void setUsers(Set<Long> users) {
		this.users = users;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
