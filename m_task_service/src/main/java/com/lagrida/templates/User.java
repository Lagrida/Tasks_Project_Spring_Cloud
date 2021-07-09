package com.lagrida.templates;

public class User {
	private long id;
	private String username;
	private String image;
	
	public User() {
	}
	
	public User(long id, String username, String image) {
		super();
		this.id = id;
		this.username = username;
		this.image = image;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
