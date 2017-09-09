package com.sparcs.kiosk.executive.account;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import lombok.ToString;

@Component
@ToString
public class BalanceTracker {

	private static final Logger LOG = LoggerFactory.getLogger(BalanceTracker.class);

	public static final String TOPIC_NAME = "/topic/account/balance";

    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    BalanceTracker(SimpMessageSendingOperations messagingTemplate) {
    	
    	this.messagingTemplate = messagingTemplate;
    }
    
	private int balance;
	
	public synchronized int getBalance() {
		
		return balance;
	}
	
	@EventHandler
	void on(EBalanceChanged event) {
		
		LOG.trace("on(event={})", event);

		publish(event.getBalance());
	}

	private synchronized void publish(int balance) {

		LOG.debug("Publishing balance={}", balance);
		messagingTemplate.convertAndSend(TOPIC_NAME, balance);
	}

}
