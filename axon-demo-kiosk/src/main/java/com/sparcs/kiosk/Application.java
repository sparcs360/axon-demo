package com.sparcs.kiosk;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sparcs.kiosk.executive.CStartKiosk;

@SpringBootApplication
public class Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {

        SpringApplication.run(Application.class, args);        
	}
	
	@Bean
	CommandLineRunner initialise(CommandBus commandBus, @Value("${kiosk.id}") String kioskId) {
		
		return((args) -> {
	        CStartKiosk cmd = new CStartKiosk(kioskId);
			LOG.trace("Dispatching {}...", cmd);
			// http://localhost:8080/console
			// jdbc:h2:mem:axon3db;MODE=Oracle;DB_CLOSE_ON_EXIT=FALSE
			commandBus.dispatch(GenericCommandMessage.asCommandMessage(cmd));
			LOG.info("Kiosk #{} Ready", kioskId);
		});
	}
}
