package com.banquito.marca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MarcaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarcaApplication.class, args);
	}
}
