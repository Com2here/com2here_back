package com.com2here.com2hereback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.com2here.com2hereback.client") // Feign 클라이언트가 있는 패키지 경로
public class Com2herebackApplication {

	public static void main(String[] args) {
		SpringApplication.run(Com2herebackApplication.class, args);
	}

}
