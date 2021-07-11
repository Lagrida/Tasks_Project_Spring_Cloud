package com.lagrida.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Signature {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long userSigned;
	
	private String file;
	
	@ColumnDefault(value="CURRENT_TIMESTAMP")
	@CreationTimestamp
	private LocalDateTime createdOn;

	public Signature() {
		
	}
	
	public Signature(long userSigned, String file) {
		this.userSigned = userSigned;
		this.file = file;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserSigned() {
		return userSigned;
	}
	public void setUserSigned(long userSigned) {
		this.userSigned = userSigned;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
}
