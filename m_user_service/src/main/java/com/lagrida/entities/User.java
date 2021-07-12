package com.lagrida.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lagrida.security.auth.Roles;

@Entity
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique=true, length=40)
	@NotNull(message = "username may not be null", groups = { ValidateInsertion.class })
	@Size(min=4, max=20, message="username must have 4 to 20 caracteres", groups = { ValidateInsertion.class })
	private String username;
	
	
	@NotNull(message = "password may not be null", groups = { ValidateInsertion.class })
	@NotEmpty(message = "password may not be null", groups = { ValidateInsertion.class })
	@Size(min=6, max=32, message="password must have 6 caracteres min", groups = { ValidateInsertion.class })
	@Transient
	private String password;
	
	@Column(length=100)
	@JsonIgnore
	private String passwordEncrypted;
	
	@Column(length=60)
	private String image;
	
	@Column(length=25)
	private String name;
	
	@Column(unique=true, length=60)
	@NotNull(message = "email may not be null", groups = { ValidateInsertion.class, ValidateUpdate.class })
	@Email(message="email not valid", groups = { ValidateInsertion.class, ValidateUpdate.class })
	@NotEmpty(message = "email may not be null", groups = { ValidateInsertion.class, ValidateUpdate.class })
	private String email;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm", iso = ISO.DATE_TIME)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime birthday;
	
	@Enumerated(EnumType.ORDINAL)
	private Gender gender;
	
    @ElementCollection(targetClass = Roles.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    Set<Roles> roles = new HashSet<>();
    
	@Column(name = "created_on", insertable=false, updatable=false)
	@ColumnDefault(value="CURRENT_TIMESTAMP")
	@CreationTimestamp
	private LocalDateTime createdOn;
	
	@UpdateTimestamp
	private LocalDateTime updatedOn;
	
	public User() {
	}

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	public User(String username, String password, String email, LocalDateTime birthday, Gender gender) {
		this(username, password, email);
		this.birthday = birthday;
		this.gender = gender;
	}
	public User(String username, String passwordEncrypted, String image, String name, String email, LocalDateTime birthday, Gender gender, Set<Roles> roles) {
		this.username = username;
		this.passwordEncrypted = passwordEncrypted;
		this.image = image;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.gender = gender;
		this.roles = roles;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDateTime birthday) {
		this.birthday = birthday;
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

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordEncrypted() {
		return passwordEncrypted;
	}

	public void setPasswordEncrypted(String passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
	}
	
}
