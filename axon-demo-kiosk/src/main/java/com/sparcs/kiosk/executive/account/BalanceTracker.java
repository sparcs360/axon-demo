package com.sparcs.kiosk.executive.account;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.ToString;

@Component
@ToString
public class BalanceTracker {

	private static final Logger LOG = LoggerFactory.getLogger(BalanceTracker.class);

    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    BalanceTracker(SimpMessageSendingOperations messagingTemplate) {
    	
    	this.messagingTemplate = messagingTemplate;
    }
    
	@Getter
	private int balance;
	
	@EventHandler
	void on(EBalanceChanged event) {
		
		LOG.trace("on(event={})", event);

		balance = event.getBalance();
		LOG.debug("Publishing balance={}", balance);
		messagingTemplate.convertAndSend("/topic/account/balance", balance);
	}

}
