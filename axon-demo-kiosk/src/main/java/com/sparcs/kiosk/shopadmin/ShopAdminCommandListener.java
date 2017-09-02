package com.sparcs.kiosk.shopadmin;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.sparcs.kiosk.ShopAdminCommand;

@Component
public class ShopAdminCommandListener {

	private static final Logger LOG = LoggerFactory.getLogger(ShopAdminCommandListener.class);

	private final CommandGateway commandGateway;

	ShopAdminCommandListener(CommandGateway commandGateway) {
		
		this.commandGateway = commandGateway;
	}
	
	@RabbitListener(queues="${kiosk.shop-amqp.queue-name}")
	void on(ShopAdminCommand command) {
		
		LOG.info("on(command={})", command);
		commandGateway.send(command);
	}
}
