package com.lagrida.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lagrida.entities.Signature;
import com.lagrida.entities.Task;
import com.lagrida.microservices.FileService;
import com.lagrida.microservices.UserService;
import com.lagrida.repositories.SignatureRepository;
import com.lagrida.repositories.TaskRepository;
import com.lagrida.security.jwt.JwtUtility;
import com.lagrida.services.RestException;
import com.lagrida.services.TaskRequest;
import com.lagrida.templates.TaskEager;
import com.lagrida.templates.TaskFile;
import com.lagrida.templates.User;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/tasks")
public class PrincipalController {
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private SignatureRepository signatureRepository;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/get_task/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public Task getTask(@PathVariable long id, HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		boolean isAdmin = jwtUtility.isAdmin(token);
		//---------------------------------------------------------------------------------------
		Task task = taskRepository.findById(id).orElseThrow(() -> new RestException("task not found", HttpStatus.NOT_FOUND));
		if(isAdmin || task.getUsers().contains(UserIdAuthentificated)) {
			return task;
		}
		throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
	}
	@PatchMapping("/change_task_type/{taskId}/{type}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public Task updateTaskType(@PathVariable long taskId, @PathVariable int type, HttpServletRequest request) {
		List<Integer> allowedTypes = Arrays.asList(0, 1);
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		boolean isAdmin = jwtUtility.isAdmin(token);
		//---------------------------------------------------------------------------------------
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RestException("task not found", HttpStatus.NOT_FOUND));
		if(allowedTypes.contains(type)) {
			task.setType(type);
			if(isAdmin || task.getUsers().contains(UserIdAuthentificated)) {
				return taskRepository.save(task);
			}
		}
		throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/get_full_tasks")
	@PreAuthorize("hasRole('ROLE_USER')")
	public List<TaskEager> getFullTasks(HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		boolean isAdmin = jwtUtility.isAdmin(token);
		//---------------------------------------------------------------------------------------
		List<Task> tasks;
		if(isAdmin) {
			tasks = taskRepository.findAllByOrderByIdDesc();
		}else {
			tasks = taskRepository.getAllTasksToUser(UserIdAuthentificated);
		}
		List<TaskEager> fullTasks = new ArrayList<>();
		tasks.forEach(task -> {
			List<TaskFile> files = Arrays.asList(fileService.getTaskFiles(task.getFiles(), token));
			List<User> users = Arrays.asList(userService.getTaskUsers(task.getUsers()));
			TaskEager fullTask = new TaskEager(task, users, files);
			
			fullTasks.add(fullTask);
		});
		return fullTasks;
	}
	/*@GetMapping("/get_full_task/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public TaskEager getFullTask(@PathVariable long id, HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		Task task = taskRepository.findById(id).orElseThrow(() -> new RestException("task not found", HttpStatus.NOT_FOUND));
		List<TaskFile> files = Arrays.asList(fileService.getTaskFiles(task.getFiles(), token));
		List<User> users = Arrays.asList(userService.getTaskUsers(task.getUsers()));
		TaskEager fullTask = new TaskEager(task, users, files);
		
		return fullTask;
	}*/
	
	@PostMapping("/add_task")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Task addTask(@Valid @RequestBody Task task, HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		logger.info("Users : {}", task.getUsers().size());
		logger.info("user info : {}", task.getUsers());
		Task taskInfo = new Task();
		taskInfo.setTitle(task.getTitle());
		taskInfo.setDescription(task.getDescription());
		taskInfo.setUserOwner(UserIdAuthentificated);
		taskInfo.setUsers(task.getUsers());
		
		return taskRepository.save(taskInfo);
	}
	@PatchMapping("/update_task/{taskId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Task updateTask(@PathVariable long taskId, @Valid @RequestBody Task task, HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		Task taskInfo = taskRepository.findById(taskId).orElseThrow(() -> new RestException("Task not found", HttpStatus.NOT_FOUND));
		if(taskInfo.getUserOwner() != UserIdAuthentificated) {
			throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
		}
		logger.info("Task ID : {}", taskInfo.getId());
		taskInfo.setTitle(task.getTitle());
		taskInfo.setDescription(task.getDescription());
		taskInfo.setUsers(task.getUsers());
		
		return taskRepository.save(taskInfo);
	}
	@DeleteMapping("/delete_task/{taskId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable long taskId, HttpServletRequest request){
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RestException("task not found", HttpStatus.NOT_FOUND));
		if(task.getUserOwner() != UserIdAuthentificated) {
			throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
		}
		fileService.deleteTaskFiles(task.getFiles(), token);
		taskRepository.delete(task);
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Task Deleted succefuly");
		return ResponseEntity.ok().body(response);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add_file_2_task/{taskId}/{fileName}")
	public ResponseEntity<Map<String, Object>> addFile2Task(@PathVariable long taskId, @PathVariable String fileName, HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RestException("task not found", HttpStatus.NOT_FOUND));
		if(task.getUserOwner() != UserIdAuthentificated) {
			throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
		}
		Set<String> files = task.getFiles();
		files.add(fileName);
		task.setFiles(files);
		taskRepository.save(task);
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "File added successfuly");
	    return ResponseEntity.ok().body(response);
	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/add_signature_2_task/{taskId}/{fileName}")
	@Transactional
	public ResponseEntity<Map<String, Object>> addSignature2Task(@PathVariable long taskId, @PathVariable String fileName, HttpServletRequest request) {
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		boolean isAdmin = jwtUtility.isAdmin(token);
		//---------------------------------------------------------------------------------------
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RestException("Task not found", HttpStatus.NOT_FOUND));
		if(isAdmin || task.getUsers().contains(UserIdAuthentificated)) {
			Signature signature = new Signature(UserIdAuthentificated, fileName);
			signatureRepository.save(signature);
			task.setSignature(signature);
			task.setType(2);
			taskRepository.save(task);
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "File added successfuly");
		    return ResponseEntity.ok().body(response);
		}else {
			throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
		}
	}
}
