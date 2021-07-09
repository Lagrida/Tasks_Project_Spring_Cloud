package com.lagrida.microservices;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lagrida.templates.TaskFile;
import com.lagrida.templates.User;

@Service
public class UserService {
	
	private static final String USER_SERVICE = "http://user-service/";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public User[] getTaskUsers(Set<Long> usersIds) {
		if(usersIds.size() == 0) {
			User users[] = {};
			return users;
		}
		String getUsers = "";
		for(long userId: usersIds) {
			getUsers += userId + ",";
		}
		if(usersIds.size() > 0) {
			getUsers = getUsers.substring(0, (getUsers.length() - 1));
		}
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		ResponseEntity<User[]> response = restTemplate.exchange(USER_SERVICE + "users/get_users_ids/" + getUsers, HttpMethod.GET, entity, User[].class);
		return response.getBody();
	}
}
