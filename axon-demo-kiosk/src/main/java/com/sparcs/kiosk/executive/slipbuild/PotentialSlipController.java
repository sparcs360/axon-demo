package com.sparcs.kiosk.executive.slipbuild;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/slip")
public class PotentialSlipController {

	private static final Logger LOG = LoggerFactory.getLogger(PotentialSlipController.class);

    private final PotentialSlipTracker potentialSlipTracker;
    
    @Autowired
    PotentialSlipController(PotentialSlipTracker potentialSlipTracker) {

    	this.potentialSlipTracker = potentialSlipTracker;
    }
    
    @MessageMapping("/get")
    @SendTo(PotentialSlipTracker.TOPIC_NAME)
    public PotentialSlip getPotentialSlip(Message<Object> message) {

    	LOG.debug("getPotentialSlip({})", message);
    	return potentialSlipTracker.getPotentialSlip();
    }
}
