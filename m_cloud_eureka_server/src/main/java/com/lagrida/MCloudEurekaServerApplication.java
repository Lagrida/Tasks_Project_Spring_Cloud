package com.lagrida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MCloudEurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MCloudEurekaServerApplication.class, args);
	}

}
