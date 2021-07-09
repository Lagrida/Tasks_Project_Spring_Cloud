package com.lagrida.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lagrida.services.RestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtTokenSecret jwtTokenSecret;
		
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.substring(7);
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtTokenSecret.getSecret().getBytes())
                    .build()
                    .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            List<String> list = (List<String>) body.get("authorities", ArrayList.class);
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = list.stream()
                    .map(m -> new SimpleGrantedAuthority(m))
                    .collect(Collectors.toSet());
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            		username,
                    null,
                    simpleGrantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }catch (JwtException e) {
        	throw new RestException("Token expired or not trusted", HttpStatus.UNAUTHORIZED);
        }
	}
}
