package com.lagrida.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lagrida.security.auth.Roles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtUtility {
	@Autowired
	private JwtTokenSecret jwtTokenSecret;
	
	public Claims getJwsBody(String token) throws JwtException {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(jwtTokenSecret.getSecret().getBytes())
                .build()
                .parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
	}
	
	public long getUserIdAuthentificated(String token) {
		Claims body = getJwsBody(token);
		long UserIdAuthentificated = (long)((int) body.get("id"));
		return UserIdAuthentificated;
	}
	public boolean isAdmin(String token) {
		Claims body = getJwsBody(token);
		List<String> list = (List<String>) body.get("authorities", ArrayList.class);
		return list.contains(Roles.ROLE_ADMIN.name());
	}
}
