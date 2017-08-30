package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/slip")
public class SlipController {

	private static final Logger LOG = LoggerFactory.getLogger(SlipController.class);
	
	private final String kioskId;
    private final CommandGateway commandGateway;
    
    @Autowired
    SlipController(@Value("${kiosk.id}") String kioskId, CommandGateway commandGateway) {

    	this.kioskId = kioskId;
    	this.commandGateway = commandGateway;
    }
    
    @MessageMapping("/selection/add")
    public void addSelection(Message<CAddSelection> message) throws Exception {

    	LOG.debug("addSelection({})", message);
    	CAddSelection messageWithIdentifier = message.getPayload().toBuilder().kioskId(kioskId).build();
		commandGateway.send(messageWithIdentifier);
    }

    @MessageMapping("/selection/remove")
    public void removeSelection(Message<CRemoveSelection> message) throws Exception {

    	LOG.debug("removeSelection({})", message);
    	CRemoveSelection messageWithIdentifier = message.getPayload().toBuilder().kioskId(kioskId).build();
		commandGateway.send(messageWithIdentifier);
    }
}
