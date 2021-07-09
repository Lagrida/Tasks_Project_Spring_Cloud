package com.lagrida.security.auth;

public enum Roles {
	ROLE_USER("USER"),
	ROLE_MONITOR("MONITOR"),
	ROLE_ADMIN("ADMIN");
	
	public final String role;
	
    private Roles(String role) {
        this.role = role;
    }
}
