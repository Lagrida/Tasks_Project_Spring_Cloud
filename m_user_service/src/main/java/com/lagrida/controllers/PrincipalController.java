package com.lagrida.controllers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lagrida.entities.User;
import com.lagrida.repositories.UserRepository;
import com.lagrida.security.auth.Roles;
import com.lagrida.security.jwt.JwtRequest;
import com.lagrida.security.jwt.JwtResponse;
import com.lagrida.security.jwt.JwtUtility;
import com.lagrida.services.RestException;

@RestController
@RequestMapping("/users")
public class PrincipalController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	@GetMapping("/test")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String test() {
		return "a resource";
	}
	@PostMapping("/auth")
	public JwtResponse auth(@RequestBody JwtRequest jwtRequest) {
		User user = userRepository.findByUsername(jwtRequest.getUsername()).orElseThrow(
				() -> new RestException("username not found", HttpStatus.BAD_REQUEST)
		);
		if(passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())) {
			String JwtToken = jwtUtility.getToken(user);
			return new JwtResponse(JwtToken, LocalDateTime.now(), LocalDateTime.now());
		}
		throw new RestException("Password not correct", HttpStatus.BAD_REQUEST);
	}
	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	@PostMapping("/addUser")
	public User addUser(@Valid @RequestBody User user) {
		User userInfo = new User();
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.USER);
		String password = passwordEncoder.encode(user.getPassword());
		
		userInfo.setRoles(roles);
		userInfo.setGender(user.getGender());
		userInfo.setBirthday(user.getBirthday());
		userInfo.setEmail(user.getEmail());
		userInfo.setImage(user.getImage());
		userInfo.setUsername(user.getUsername());
		userInfo.setPassword(password);
		return userRepository.save(userInfo);
	}
	@GetMapping("/getUser/{id}")
	public User getUser(@PathVariable long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RestException("user not found", HttpStatus.NOT_FOUND));
		logger.info("this is user : {}", user.getUsername());
		return user;
	}
}
