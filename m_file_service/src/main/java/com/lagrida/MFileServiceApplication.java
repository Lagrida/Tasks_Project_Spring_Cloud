package com.lagrida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class MFileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MFileServiceApplication.class, args);
	}
	@LoadBalanced
	@Bean
	public RestTemplate restemplate() {
		return new RestTemplate();
		/*return new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
	        request.getHeaders().add("Content-Type", "application/json");
	        request.getHeaders().add("Accept", "application/json");
	        return execution.execute(request, body);
	    })).build();*/
	}
}
