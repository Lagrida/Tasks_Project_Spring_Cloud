package com.lagrida.microservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class FileService {
	
	private static final String FILE_SERVICE = "http://file-service/";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public void deleteTaskFiles(Set<String> fileNames, String bearerToken) {
		if(fileNames.size() == 0) {
			return;
		}
		String getFileNames = "";
		for(String fileName: fileNames) {
			getFileNames += fileName + ",";
		}
		getFileNames = getFileNames.substring(0, (getFileNames.length() - 1));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(bearerToken);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		restTemplate.exchange(FILE_SERVICE + "files/delete_files/" + getFileNames, HttpMethod.DELETE, entity, Object.class);
	}
	public TaskFile getTaskFile(String fileName, String bearerToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(bearerToken);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		ResponseEntity<TaskFile> response = restTemplate.exchange(FILE_SERVICE + "files/get_file/" + fileName, HttpMethod.GET, entity, TaskFile.class);
		return response.getBody();
	}
	public TaskFile[] getTaskFiles(Set<String> fileNames, String bearerToken) {
		if(fileNames.size() == 0) {
			TaskFile files[] = {};
			return files;
		}
		String getFileNames = "";
		for(String fileName: fileNames) {
			getFileNames += fileName + ",";
		}
		getFileNames = getFileNames.substring(0, (getFileNames.length() - 1));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(bearerToken);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);
		ResponseEntity<TaskFile[]> response = restTemplate.exchange(FILE_SERVICE + "files/get_files/" + getFileNames, HttpMethod.GET, entity, TaskFile[].class);
		return response.getBody();
	}
	/*public void uploadFile(long id) {
		Map<String, Long> map = new HashMap<>();
		map.put("id", id);
		ResponseEntity<User> userResponse = restTemplate.getForEntity(FILE_SERVICE + "users/getUser/{id}", User.class, map);
		restTemplate.postForEntity(url, request, responseType, uriVariables);
		
	}*/
}
