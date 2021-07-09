package com.lagrida.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
public class JwtTokenSecret {
	private String secret;
	private long validity;
	
	public JwtTokenSecret() {
	}
	
	public JwtTokenSecret(String secret) {
		this.secret = secret;
	}
	public JwtTokenSecret(String secret, long validity) {
		this(secret);
		this.validity = validity;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}
	
	
}
