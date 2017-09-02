package com.sparcs.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {

        SpringApplication.run(Application.class, args);        
	}
	
	@Bean
	CommandLineRunner initialise(@Value("${counter.shop.id}") String shopId) {
		
		return((args) -> {
			LOG.info("Counter in Shop #{} Ready", shopId);
		});
	}
}
