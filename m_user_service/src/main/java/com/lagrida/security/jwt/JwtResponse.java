package com.lagrida.security.jwt;

import java.time.LocalDateTime;

public class JwtResponse {
	private String token;
	private LocalDateTime issuedAt;
	private LocalDateTime expirationAt;
	
	public JwtResponse() {

	}
	public JwtResponse(String token, LocalDateTime issuedAt, LocalDateTime expirationAt) {
		this.issuedAt = issuedAt;
		this.expirationAt = expirationAt;
		this.token = token;
	}
	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}
	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
	}
	public LocalDateTime getExpirationAt() {
		return expirationAt;
	}
	public void setExpirationAt(LocalDateTime expirationAt) {
		this.expirationAt = expirationAt;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
