package com.sparcs.counter.kioskeventprocessors;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.sparcs.kiosk.executive.EKioskReset;
import com.sparcs.kiosk.executive.EKioskStarted;
import com.sparcs.kiosk.executive.account.EBalanceChanged;

import lombok.Data;

@Component
public class KioskMonitor {
    
	private static final Logger LOG = LoggerFactory.getLogger(KioskMonitor.class);

    private final SimpMessageSendingOperations uiMessagingTemplate;
    private final Map<String, KioskStatus> kioskStatusMap;
    
    @Autowired
    public KioskMonitor(SimpMessageSendingOperations uiMessagingTemplate) {
    	
    	this.uiMessagingTemplate = uiMessagingTemplate;
    	kioskStatusMap = new ConcurrentHashMap<>();
    }
    
    @EventHandler
    void on(EKioskStarted event) {

        LOG.trace("{}", event);
        KioskStatus kioskStatus = new KioskStatus(event.getKioskId());
		kioskStatusMap.put(event.getKioskId(), kioskStatus);
		
		publishToUI(kioskStatus);
    }

    @EventHandler
    void on(EKioskReset event) {

        LOG.trace("{}", event);
    }

    @EventHandler
    void on(EBalanceChanged event) {

        LOG.trace("{}", event);
        KioskStatus kioskStatus = kioskStatusMap.get(event.getKioskId());
        if (kioskStatus == null) {
        	kioskStatus = new KioskStatus(event.getKioskId());
            kioskStatusMap.put(event.getKioskId(), kioskStatus);
        }
        kioskStatus.setBalance(event.getBalance());

        publishToUI(kioskStatus);
    }

	public List<KioskStatus> getAllKioskStatuses() {

		return kioskStatusMap.values().stream()
				.sorted(Comparator.comparing(KioskStatus::getKioskId))
				.collect(Collectors.toList());
	}

	public Optional<KioskStatus> getKioskStatus(String kioskId) {

		return Optional.ofNullable(kioskStatusMap.get(kioskId));
	}

	private void publishToUI(KioskStatus kioskStatus) {

		uiMessagingTemplate.convertAndSend("/topic/kiosks/events", getAllKioskStatuses());
	}

    @Data
    public static class KioskStatus {
    	
    	private final String kioskId;
    	private int balance;
    	
    	public KioskStatus(String kioskId) {
    		
    		this.kioskId = kioskId;
    		this.balance = 0;
    	}
    }
}
