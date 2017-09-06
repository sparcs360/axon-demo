package com.sparcs.counter.shopadmin;

import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.sparcs.counter.config.CounterProperties;
import com.sparcs.counter.kioskeventprocessors.KioskMonitor;
import com.sparcs.counter.kioskeventprocessors.KioskMonitor.KioskStatus;

@Controller
@MessageMapping("/kiosks")
public class KiosksController {

	private static final Logger LOG = LoggerFactory.getLogger(KiosksController.class);

	private final CounterProperties counterProperties;
	private final KioskMonitor kioskMonitor;
    private final CommandGateway commandGateway;

    @Autowired
    public KiosksController(CounterProperties counterProperties, KioskMonitor kioskMonitor, CommandGateway commandGateway) {

    	this.counterProperties = counterProperties;
    	this.kioskMonitor = kioskMonitor;
        this.commandGateway = commandGateway;
    }

    @SubscribeMapping("/get-status")
    @SendTo("/topic/kiosks/events")
    public List<KioskStatus> getStatus(Message<Object> message) {

    	LOG.debug("getStatus({})", message);
    	return kioskMonitor.getAllKioskStatuses();
    }
}
