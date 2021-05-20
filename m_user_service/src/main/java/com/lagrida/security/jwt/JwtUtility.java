package com.lagrida.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lagrida.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtility {
	@Autowired
	private JwtTokenSecret jwtTokenSecret;
	
	public String getToken(User user) {
		String token = Jwts.builder()
				.claim("id", user.getId())
				.setSubject(user.getUsername())
				.claim("email", user.getEmail())
				.claim("image", user.getImage())
				.claim("authorities", user.getRoles())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtTokenSecret.getValidity()))
				.signWith(Keys.hmacShaKeyFor(jwtTokenSecret.getSecret().getBytes()))
				.compact();
				
		return token;
	}
}
