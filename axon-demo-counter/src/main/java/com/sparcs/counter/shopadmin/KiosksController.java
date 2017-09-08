package com.sparcs.counter.shopadmin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparcs.counter.config.CounterProperties;
import com.sparcs.counter.kioskeventprocessors.KioskMonitor;
import com.sparcs.counter.kioskeventprocessors.KioskMonitor.KioskStatus;
import com.sparcs.kiosk.IKioskCommand;
import com.sparcs.kiosk.ICounterToKioskCommand;

@Controller
@MessageMapping("/kiosks")
public class KiosksController {

	private static final Logger LOG = LoggerFactory.getLogger(KiosksController.class);

	private final CounterProperties counterProperties;
	private final AmqpTemplate amqpTemplate;
	private final KioskMonitor kioskMonitor;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public KiosksController(CounterProperties counterProperties, AmqpTemplate amqpTemplate, KioskMonitor kioskMonitor) {

		this.counterProperties = counterProperties;
		this.amqpTemplate = amqpTemplate;
    	this.kioskMonitor = kioskMonitor;
        
        this.objectMapper = new ObjectMapper();
    }

    @SubscribeMapping("/get-status")
    @SendTo("/topic/kiosks/events")
    public List<KioskStatus> getStatus(Message<Object> message) {

    	LOG.debug("getStatus({})", message);
    	return kioskMonitor.getAllKioskStatuses();
    }

    @MessageMapping("/sendcmd/{name}")
    public void sendCommand(@DestinationVariable String name, String payload) {

    	LOG.debug("sendCommand(name={}, payload={})", name, payload);
    	IKioskCommand command = null;
		try {
	    	Class<? extends ICounterToKioskCommand> commandClass = findCommandByName(name);
			command = objectMapper.readValue(payload, commandClass);
		} catch (Exception e) {
			LOG.error("Failed to deserialise payload for command", e);
			return;
		}
		
		amqpTemplate.convertAndSend(counterProperties.getAmqpKioskCommandsOutExchangeName(), command.getKioskId(), command);
    }

	private Class<? extends ICounterToKioskCommand> findCommandByName(String name) throws ClassNotFoundException {
		
		String fullClassName = String.format("%s.%s", ICounterToKioskCommand.class.getPackage().getName(), name);
		@SuppressWarnings("unchecked")
		Class<? extends ICounterToKioskCommand> commandClass = (Class<? extends ICounterToKioskCommand>) Class.forName(fullClassName);
		return commandClass;
	}
}
