package com.lagrida.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lagrida.security.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtFilter jwtFilter;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		.csrf().disable()
    		.authorizeRequests()
    		.anyRequest().permitAll()
    		.and()
    		.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
    		//.antMatchers("/", "/users/auth", "/users/getAllUsers").permitAll()
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
			//.authorizeRequests()
			//.anyRequest().authenticated();
    }
}
