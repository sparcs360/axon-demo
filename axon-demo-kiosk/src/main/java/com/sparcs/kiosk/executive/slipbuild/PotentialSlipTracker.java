package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.spring.config.AxonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.sparcs.kiosk.config.KioskProperties;
import com.sparcs.kiosk.executive.ExecutiveAggregate;

import lombok.ToString;

@Component
@ToString
public class PotentialSlipTracker {

	private static final Logger LOG = LoggerFactory.getLogger(PotentialSlipTracker.class);

	public static final String TOPIC_NAME = "/topic/slip";

	private final KioskProperties kioskProperties;
	private final Repository<ExecutiveAggregate> executiveAggregateRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    PotentialSlipTracker(KioskProperties kioskProperties, AxonConfiguration axonConfiguration, SimpMessageSendingOperations messagingTemplate) {
    	
    	this.kioskProperties = kioskProperties;
    	this.executiveAggregateRepository = axonConfiguration.repository(ExecutiveAggregate.class);
    	this.messagingTemplate = messagingTemplate;
    }

    private PotentialSlip potentialSlip;
    
    public synchronized PotentialSlip getPotentialSlip() {

    	return potentialSlip;
    }
    
	@EventHandler
	void on(ESelectionAdded event) {
		
		LOG.trace("on(event={})", event);

		publish();
	}
	
	@EventHandler
	void on(ESelectionRemoved event) {
		
		LOG.trace("on(event={})", event);
		
		publish();
	}

	private synchronized void publish() {

		Aggregate<ExecutiveAggregate> aggregate = executiveAggregateRepository.load(kioskProperties.getKioskId());
		potentialSlip = aggregate.invoke(ExecutiveAggregate::getCopyOfPotentialSlip);
		LOG.debug("Publishing slip={}", potentialSlip);
		messagingTemplate.convertAndSend(TOPIC_NAME, potentialSlip);
	}
}
