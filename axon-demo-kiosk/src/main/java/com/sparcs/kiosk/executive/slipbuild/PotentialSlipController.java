package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.sparcs.kiosk.config.KioskProperties;

@Controller
@MessageMapping("/slip")
public class PotentialSlipController {

	private static final Logger LOG = LoggerFactory.getLogger(PotentialSlipController.class);
	
	private final KioskProperties kioskProperties;
    private final CommandGateway commandGateway;
    private final PotentialSlipTracker potentialSlipTracker;
    
    @Autowired
    PotentialSlipController(KioskProperties kioskProperties, CommandGateway commandGateway, PotentialSlipTracker potentialSlipTracker) {

    	this.kioskProperties = kioskProperties;
    	this.commandGateway = commandGateway;
    	this.potentialSlipTracker = potentialSlipTracker;
    }
    
    @MessageMapping("/get")
    @SendTo(PotentialSlipTracker.TOPIC_NAME)
    public PotentialSlip getPotentialSlip(Message<Object> message) {

    	LOG.debug("getPotentialSlip({})", message);
    	return potentialSlipTracker.getPotentialSlip();
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
