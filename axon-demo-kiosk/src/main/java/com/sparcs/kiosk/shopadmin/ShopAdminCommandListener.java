package com.sparcs.kiosk.shopadmin;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.sparcs.kiosk.ICounterToKioskCommand;

@Profile("!DisableAmqp")
@Component
public class ShopAdminCommandListener {

	private static final Logger LOG = LoggerFactory.getLogger(ShopAdminCommandListener.class);

	private final CommandGateway commandGateway;

	@Autowired
	ShopAdminCommandListener(CommandGateway commandGateway) {
		
		this.commandGateway = commandGateway;
	}

	@RabbitListener(queues="#{@distributedCommandQueue}")
	void on(ICounterToKioskCommand command) {
		
		LOG.info("on(command={})", command);
		commandGateway.send(command);
	}
}
