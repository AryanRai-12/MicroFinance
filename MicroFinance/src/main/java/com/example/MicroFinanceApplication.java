package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MicroFinanceApplication {

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(MicroFinanceApplication.class, args);
		
	}

}
