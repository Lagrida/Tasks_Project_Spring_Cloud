package com.lagrida;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.lagrida.entities.Gender;
import com.lagrida.entities.User;
import com.lagrida.repositories.UserRepository;
import com.lagrida.security.auth.Roles;

import brave.sampler.Sampler;

//import brave.sampler.Sampler;

@SpringBootApplication
@EnableDiscoveryClient
public class MUserServiceApplication {

	@LoadBalanced
	@Bean
	public RestTemplate restemplate() {
		return new RestTemplate();
	}
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MUserServiceApplication.class, args);
		
		// List of users with different roles
		/*
		UserRepository userRepository = context.getBean(UserRepository.class);
		PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
		
		String password;
		//LocalDateTime.of(year, month, dayOfMonth, hour, minute)
		LocalDateTime birthday = LocalDateTime.of(1993, 4, 4, 1, 0);
		LocalDateTime birthday2 = LocalDateTime.of(1990, 6, 9, 1, 0);
		LocalDateTime birthday3 = LocalDateTime.of(1990, 11, 2, 1, 0);
		LocalDateTime birthday4 = LocalDateTime.of(1983, 3, 25, 1, 0);
		LocalDateTime birthday5 = LocalDateTime.of(1983, 12, 22, 1, 0);
		LocalDateTime birthday6 = LocalDateTime.of(1999, 1, 2, 1, 0);
		LocalDateTime birthday7 = LocalDateTime.of(1985, 4, 13, 1, 0);
		LocalDateTime birthday8 = LocalDateTime.of(1985, 2, 7, 1, 0);
		LocalDateTime birthday9 = LocalDateTime.of(1988, 8, 11, 1, 0);
		LocalDateTime birthday10 = LocalDateTime.of(1998, 2, 21, 1, 0);
		LocalDateTime birthday11 = LocalDateTime.of(1995, 6, 11, 1, 0);
		LocalDateTime birthday12 = LocalDateTime.of(1994, 12, 2, 1, 0);
		LocalDateTime birthday13 = LocalDateTime.of(1995, 5, 7, 1, 0);
		
		Set<Roles> roles = new HashSet<>();
		roles.add(Roles.ROLE_ADMIN);
		roles.add(Roles.ROLE_MONITOR);
		roles.add(Roles.ROLE_USER);
		Set<Roles> roles2 = new HashSet<>();
		roles2.add(Roles.ROLE_MONITOR);
		roles2.add(Roles.ROLE_USER);
		Set<Roles> roles3 = new HashSet<>();
		roles3.add(Roles.ROLE_USER);
		
		List<User> users = new ArrayList<User>();
		
		password = passwordEncoder.encode("123456");
		User user = new User("Lagrida", password, "https://demos.lagrida.com/files/images/lagrida.png",
					"LAGRIDA Yassine", "lagyassine@gmail.com", birthday, Gender.MALE, roles);
		users.add(user);
		
		password = passwordEncoder.encode("123456");
		User user0 = new User("Euler", password, "https://demos.lagrida.com/files/images/euler.png",
				"Leonhard Euler", "euler@gmail.com", birthday2, Gender.MALE, roles2);
		users.add(user0);
		
		password = passwordEncoder.encode("123456");
		User user1 = new User("Gauss", password, "https://demos.lagrida.com/files/images/gauss.png",
				"Carl Friedrich Gauss", "gauss@gmail.com", birthday3, Gender.MALE, roles2);
		users.add(user1);
		
		password = passwordEncoder.encode("123456");
		User user2 = new User("Ahmed", password, "",
				"LAGRIDA Yassine", "ahmed@gmail.com", birthday4, Gender.MALE, roles);
		users.add(user2);
		
		password = passwordEncoder.encode("123456");
		User user3 = new User("Sara_2", password, "https://lagrida.com/files/forums/img7.png",
				"Sara Tancredi", "sara_tancredi@gmail.com", birthday5, Gender.FEMALE, roles3);
		users.add(user3);
		
		password = passwordEncoder.encode("123456");
		User user4 = new User("John", password, "https://demos.lagrida.com/files/images/lagrida.png",
				"John Snow", "john_snow@gmail.com", birthday6, Gender.MALE, roles3);
		users.add(user4);
		
		password = passwordEncoder.encode("123456");
		User user5 = new User("Albertit", password, "https://lagrida.com/files/forums/img5.png",
				"Albert Tao", "albertit@gmail.com", birthday7, Gender.MALE, roles3);
		users.add(user5);
		
		password = passwordEncoder.encode("123456");
		User user6 = new User("Google", password, "https://lagrida.com/files/forums/img6.png",
				"Google Goo", "google@gmail.com", birthday8, Gender.MALE, roles3);
		users.add(user6);
		
		password = passwordEncoder.encode("123456");
		User user7 = new User("Karima", password, "",
				"Karima El", "karima_el@gmail.com", birthday9, Gender.FEMALE, roles3);
		users.add(user7);
		
		password = passwordEncoder.encode("123456");
		User user8 = new User("Peter", password, "https://lagrida.com/files/forums/img3.png",
				"Peter spider man", "spider@gmail.com", birthday10, Gender.MALE, roles3);
		users.add(user8);
		
		password = passwordEncoder.encode("123456");
		User user9 = new User("Alfred", password, "",
				"Alfred Redo", "alfred_222@gmail.com", birthday11, Gender.MALE, roles3);
		users.add(user9);
		
		password = passwordEncoder.encode("123456");
		User user10 = new User("Michel", password, "https://lagrida.com/files/forums/img9.png",
				"Michel Raddi", "michel_55@gmail.com", birthday12, Gender.MALE, roles3);
		users.add(user10);
		
		password = passwordEncoder.encode("123456");
		User user11 = new User("Sami", password, "https://lagrida.com/files/forums/img10.png",
				"Samoel Sami", "samoel_sami@gmail.com", birthday13, Gender.MALE, roles3);
		users.add(user11);
		
		password = passwordEncoder.encode("123456");
		User user12 = new User("Spring", password, "",
				"Spring boot", "spring_boot@gmail.com", birthday3, Gender.MALE, roles3);
		users.add(user12);
		
		password = passwordEncoder.encode("123456");
		User user13 = new User("Angular", password, "",
				"Angular Google", "angular_google@gmail.com", birthday8, Gender.MALE, roles3);
		users.add(user13);
		
		userRepository.saveAll(users);
		*/
	}
}
