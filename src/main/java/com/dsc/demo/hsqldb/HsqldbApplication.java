package com.dsc.demo.hsqldb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HsqldbApplication {

	public static void main(String[] args) {
		SpringApplication.run(HsqldbApplication.class, args);
	}

}
