package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.sparcs.kiosk.config.KioskProperties;

@Controller
@MessageMapping("/slip")
public class SlipController {

	private static final Logger LOG = LoggerFactory.getLogger(SlipController.class);
	
	private final KioskProperties kioskProperties;
    private final CommandGateway commandGateway;
    
    @Autowired
    SlipController(KioskProperties kioskProperties, CommandGateway commandGateway) {

    	this.kioskProperties = kioskProperties;
    	this.commandGateway = commandGateway;
    }
    
    @MessageMapping("/selection/add")
    public void addSelection(Message<CAddSelection> message) throws Exception {

    	LOG.debug("addSelection({})", message);
    	CAddSelection messageWithIdentifier = message.getPayload().toBuilder().kioskId(kioskProperties.getKioskId()).build();
		commandGateway.send(messageWithIdentifier);
    }

    @MessageMapping("/selection/remove")
    public void removeSelection(Message<CRemoveSelection> message) throws Exception {

    	LOG.debug("removeSelection({})", message);
    	CRemoveSelection messageWithIdentifier = message.getPayload().toBuilder().kioskId(kioskProperties.getKioskId()).build();
		commandGateway.send(messageWithIdentifier);
    }
}
