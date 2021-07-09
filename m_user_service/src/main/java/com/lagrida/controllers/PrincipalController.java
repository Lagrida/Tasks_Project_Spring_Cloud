package com.lagrida.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.lagrida.entities.User;
import com.lagrida.entities.ValidateInsertion;
import com.lagrida.entities.ValidateUpdate;
import com.lagrida.repositories.UserRepository;
import com.lagrida.security.auth.Roles;
import com.lagrida.security.jwt.JwtRequest;
import com.lagrida.security.jwt.JwtResponse;
import com.lagrida.security.jwt.JwtTokenSecret;
import com.lagrida.security.jwt.JwtUtility;
import com.lagrida.services.RestException;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/users")
public class PrincipalController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JwtTokenSecret jwtTokenSecret;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtility jwtUtility;
	
	
	@GetMapping("/")
	public String home(HttpServletRequest request) {
		//logger.info("header : {}", request.getHeader("Authorization"));
		return "hello world";
	}
	/*@GetMapping("/test")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String test() {
		return "a resource need admin role";
	}*/
	@PostMapping("/auth")
	public Map<String, Object> auth(@RequestBody JwtRequest jwtRequest) throws JsonProcessingException {
		User user = userRepository.findByUsername(jwtRequest.getUsername()).orElseThrow(
				() -> new RestException("username not found", HttpStatus.BAD_REQUEST)
		);
		if(passwordEncoder.matches(jwtRequest.getPassword(), user.getPasswordEncrypted())) {
			
			// filtring password
			SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("password");
			
			FilterProvider filters = new SimpleFilterProvider().addFilter("filterPassword", filter);
			
			MappingJacksonValue mapping = new MappingJacksonValue(user);
			mapping.setFilters(filters);

			String JwtToken = jwtUtility.getToken(user);
			Map<String, Object> response = new HashMap<>();
			response.put("user", mapping.getValue());
			response.put("auth", new JwtResponse(JwtToken, LocalDateTime.now(), LocalDateTime.now().plusSeconds(Math.floorDiv(jwtTokenSecret.getValidity(), 1000))));
			return response;
		}
		throw new RestException("Password not correct", HttpStatus.BAD_REQUEST);
	}
	@GetMapping("/get_all_users/{page}/{page_size}")
	public Page<User> getAllUsers(@PathVariable int page, @PathVariable int page_size) {
		Page<User> users = userRepository.findAll(PageRequest.of(page, page_size));
		return users;
	}
	@PostMapping("/addUser")
	public User addUser(@Valid @RequestBody User user) {
		User userInfo = new User();
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.ROLE_USER);
		String password = passwordEncoder.encode(user.getPassword());
		
		userInfo.setRoles(roles);
		userInfo.setGender(user.getGender());
		userInfo.setBirthday(user.getBirthday());
		userInfo.setEmail(user.getEmail());
		userInfo.setImage(user.getImage());
		userInfo.setUsername(user.getUsername());
		userInfo.setPasswordEncrypted(password);
		userInfo.setName(user.getName());
		return userRepository.save(userInfo);
	}
	@GetMapping("/get_user/{id}")
	public User getUser(@PathVariable long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RestException("user not found", HttpStatus.NOT_FOUND));
		logger.info("this is user : {}", user.getUsername());
		return user;
	}
	@GetMapping("/get_users_ids/{usersIds}")
	public List<User> getUsersWithIds(@PathVariable Long[] usersIds) {
		List<Long> users = Arrays.asList(usersIds);
		return userRepository.getAllUsersWithIds(users);
	}
	/*@PreAuthorize("hasRole('ROLE_USER')")
	//@PostAuthorize("returnObject.get('message') == 'Hello2'")
	@GetMapping("/example/{id}")
	public Map<String, Object> test(@PathVariable("id") long userId, HttpServletRequest request) {
		
		// stateless check of user ID
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//////////////////////////////////////////////////////////////////
		
		if(UserIdAuthentificated != userId) {
			throw new RestException("Action not Allowed", HttpStatus.UNAUTHORIZED);
		}
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("message", "Hello");
		return m;
	}*/
	@GetMapping("/get_users/{startsWith}")
	public List<Map<String, Object>> getUsersStartsWith(@PathVariable("startsWith") String startsWith){
		List<User> users = userRepository.findByUsernameStartsWith(startsWith);
		List<Map<String, Object>> list = new ArrayList<>();
		
		users.forEach(user -> {
			Map<String, Object> userDetails = new HashMap<>();
			userDetails.put("id", user.getId());
			userDetails.put("username", user.getUsername());
			userDetails.put("image", user.getImage());
			
			list.add(userDetails);
		});
		return list;
	}
	@PatchMapping("/update_profile/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public Map<String, Object> updateProfile(@Validated({ ValidateUpdate.class }) @RequestBody User userInfo, @PathVariable("id") long userId, HttpServletRequest request) throws JsonProcessingException {
		
		//----------------------------- Stateless check of user ID -------------------------------
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.substring(7);
		long UserIdAuthentificated = jwtUtility.getUserIdAuthentificated(token);
		//----------------------------------------------------------------------------------------
		if(UserIdAuthentificated != userId) {
			throw new RestException("Action not Allowed", HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findById(userId).orElseThrow(() -> new RestException("user not found", HttpStatus.NOT_FOUND));
		user.setImage(userInfo.getImage());
		user.setBirthday(userInfo.getBirthday());
		user.setName(userInfo.getName());
		user.setGender(userInfo.getGender());
		user.setEmail(userInfo.getEmail());
		
		userRepository.save(user);

		String JwtToken = jwtUtility.getToken(user);
		Map<String, Object> response = new HashMap<>();
		response.put("user", user);
		response.put("auth", new JwtResponse(JwtToken, LocalDateTime.now(), LocalDateTime.now().plusSeconds(Math.floorDiv(jwtTokenSecret.getValidity(), 1000))));
		return response;
	}
	
}
