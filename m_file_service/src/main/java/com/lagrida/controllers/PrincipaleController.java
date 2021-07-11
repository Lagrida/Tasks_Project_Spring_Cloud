package com.lagrida.controllers;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.lagrida.entities.AppFile;
import com.lagrida.microservices.TaskService;
import com.lagrida.repositories.FileRepository;
import com.lagrida.security.jwt.JwtUtility;
import com.lagrida.services.FilesService;
import com.lagrida.services.RestException;
import com.lagrida.templates.Task;

import io.jsonwebtoken.Claims;


@RestController
@RequestMapping("/files")
public class PrincipaleController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FilesService filesService;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@GetMapping("/test")
	public String test(){
		return "a test";
	}
	
	/*@PreAuthorize("hasRole('ROLE_USER')")
	public AppFile getFile(String fileName){
		
	}*/
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/get_file/{fileName}")
	public AppFile getFile(@PathVariable String fileName) {
		return fileRepository.findByFileName(fileName).orElseThrow(
				() -> new RestException("File not found", HttpStatus.NOT_FOUND));
	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/get_files/{fileNames}")
	public List<AppFile> getFiles(@PathVariable String[] fileNames) {
		List<String> files = Arrays.asList(fileNames);
		return fileRepository.getAllFilesWithFileNames(files);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete_files/{fileNames}")
	@Transactional
	public ResponseEntity<Map<String, Object>> deleteFiles(@PathVariable String[] fileNames, HttpServletRequest request){
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		List<String> files = Arrays.asList(fileNames);
		fileRepository.deleteAllFilesWithFilesNames(files, UserIdAuthentificated); // Delete files where userOwner = UserIdAuthentificated
		//----------------------------- Delete files From the directory here -------------------- 
	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "All files are deleted");
	    return ResponseEntity.ok().body(response);
	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/add_signature_task/{taskId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> addSignature2Task(@PathVariable long taskId, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, MaxUploadSizeExceededException{
		
		long fileSize = file.getSize();
		String fileOriginalName = file.getOriginalFilename();
		String fileExtension = filesService.getExtension(fileOriginalName);
		
		String filesDirectory = "tasks\\signatures";
		String serviceSource = "task-service";
		String additionalSource = "signatures";
		
		logger.info("fileName : {}", fileOriginalName);
		logger.info("extension : {}", fileExtension);
		
		AppFile appFile = new AppFile();
		appFile.setFileSize(fileSize);
		appFile.setFileOriginalName(fileOriginalName);
		appFile.setServiceSource(serviceSource);
		appFile.setAdditionalSource(additionalSource);
		
		if(!filesService.checkExtension(fileExtension)) { // banal check of extension, must do moooore
			throw new RestException("Extension not allowed", HttpStatus.FORBIDDEN, fileOriginalName);
		}
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		boolean isAdmin = jwtUtility.isAdmin(token);
		//---------------------------------------------------------------------------------------
		Task task = taskService.getTask(taskId, token);
		if(isAdmin || task.getUsers().contains(UserIdAuthentificated)) {
			
			String newFileName = filesService.generateRandomText();
			appFile.setFileExtension(fileExtension);
			appFile.setUserOwner(UserIdAuthentificated);
			appFile.setSource(task.getId());
			appFile.setFileDirectory(filesDirectory);
			
			fileRepository.save(appFile);
			newFileName = appFile.getId() + "-" + newFileName;
			filesService.uploadTheFile(file, newFileName, fileExtension, filesDirectory);
			appFile.setFileName(newFileName);
			fileRepository.save(appFile);
			
			taskService.addSignature2Task(task.getId(), appFile.getFileName(), token);
			
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "Signature added successfuly");
		    return ResponseEntity.ok().body(response);
		}else {
			throw new RestException("Action not allowed", HttpStatus.FORBIDDEN);
		}
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add_file_task/{taskId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> addFile2Task(@PathVariable long taskId, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException, MaxUploadSizeExceededException {

		//long MAX_SIZE = 1024*1024*25; // En Octet, 25 MO.
		long fileSize = file.getSize();
		String fileOriginalName = file.getOriginalFilename();
		String fileExtension = filesService.getExtension(fileOriginalName);
		
		//logger.info("File size : {}", file.getSize());
		
		/*if(file.getSize() > MAX_SIZE) {
			throw new RestException("Size is exceeded", HttpStatus.FORBIDDEN, fileOriginalName);
		}*/
		String filesDirectory = "tasks\\files";
		String serviceSource = "task-service";
		String additionalSource = "tasks";
		
		AppFile appFile = new AppFile();
		appFile.setFileSize(fileSize);
		appFile.setFileOriginalName(fileOriginalName);
		appFile.setServiceSource(serviceSource);
		appFile.setAdditionalSource(additionalSource);
		
		if(!filesService.checkExtension(fileExtension)) { // banal check of extension, must do moooore
			throw new RestException("Extension not allowed", HttpStatus.FORBIDDEN, fileOriginalName);
		}
		
		//----------------------------- Stateless getting user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//---------------------------------------------------------------------------------------
		Task task = taskService.getTask(taskId, token);
		if(task.getUserOwner() != UserIdAuthentificated) { // check if the userOwner of task who is adding the file
			throw new RestException("Action not allowed", HttpStatus.FORBIDDEN, fileOriginalName);
		}
		String newFileName = filesService.generateRandomText();
		appFile.setFileExtension(fileExtension);
		appFile.setUserOwner(UserIdAuthentificated);
		appFile.setSource(task.getId());
		appFile.setFileDirectory(filesDirectory);
		
		
		fileRepository.save(appFile);
		newFileName = appFile.getId() + "-" + newFileName;
		filesService.uploadTheFile(file, newFileName, fileExtension, filesDirectory);
		appFile.setFileName(newFileName);
		fileRepository.save(appFile);
		taskService.addFile2Task(task.getId(), appFile.getFileName(), token);
		
	    Map<String, Object> response = new HashMap<>();
	    response.put("fileName", newFileName);
	    return ResponseEntity.ok().body(response);
	}
}
