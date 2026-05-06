package com.appointment.record_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RecordServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordServiceApplication.class, args);
	}

}
