package com.lagrida.microservices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lagrida.templates.Task;

@Service
public class TaskService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String TASK_SERVICE = "http://task-service/";
	
	public Task getTask(long taskId, String bearerToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(bearerToken);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		ResponseEntity<Task> userResponse = restTemplate.exchange(TASK_SERVICE + "tasks/get_task/" + taskId, HttpMethod.GET, entity, Task.class);
		return userResponse.getBody();
	}
	public void addFile2Task(long taskId, String fileName, String bearerToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(bearerToken);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		/*Map<String, Long> map = new HashMap<>();
		map.put("taskId", taskId);
		map.put("fileId", fileId);*/
		//restTemplate.postForEntity(TASK_SERVICE + "tasks/add_file_2_task/{taskId}/{fileId}", null, null, map);
		restTemplate.exchange(TASK_SERVICE + "tasks/add_file_2_task/" + taskId + "/" + fileName, HttpMethod.POST, entity, String.class);
		//restTemplate.post
		//restTemplate.exchange(TASK_SERVICE + "tasks/add_file_2_task/{taskId}/{fileId}", HttpMethod.POST, entity, null, map);
	}
	
}
