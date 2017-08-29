package com.sparcs.kiosk.instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

@Component
public class LoggingMessagingSubProtocolEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingMessagingSubProtocolEventListener.class);
	
    @EventListener
    void on(AbstractSubProtocolEvent event) {
    	LOG.trace("on(event={})", event);
    }
}
