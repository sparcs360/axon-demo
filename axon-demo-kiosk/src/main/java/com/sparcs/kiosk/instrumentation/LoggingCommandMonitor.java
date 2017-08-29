package com.sparcs.kiosk.instrumentation;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingCommandMonitor extends LoggingMessageMonitor {

	public LoggingCommandMonitor() {
		super(LoggerFactory.getLogger(LoggingCommandMonitor.class));
	}
}
