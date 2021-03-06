package com.sparcs.kiosk;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.sparcs.kiosk.config.KioskProperties;
import com.sparcs.kiosk.executive.CStartKiosk;

@SpringBootApplication
@ComponentScan(basePackages="com.sparcs")
public class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner initialise(CommandGateway commandGateway, KioskProperties kioskProperties) {
		
		return((args) -> {

	        CStartKiosk cmd = new CStartKiosk(kioskProperties.getKioskId());
	        commandGateway.send(cmd);

			LOG.info("Kiosk #{} Ready", kioskProperties.getKioskId());
		});
	}
}
