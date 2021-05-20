package com.lagrida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

//import brave.sampler.Sampler;

@SpringBootApplication
@EnableDiscoveryClient
public class MUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MUserServiceApplication.class, args);
	}
	@LoadBalanced
	@Bean
	public RestTemplate restemplate() {
		return new RestTemplate();
	}
	/*@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}*/
}
