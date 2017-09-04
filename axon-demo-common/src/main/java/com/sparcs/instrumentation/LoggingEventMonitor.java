package com.sparcs.instrumentation;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingEventMonitor extends LoggingMessageMonitor {

	public LoggingEventMonitor() {
		super(LoggerFactory.getLogger(LoggingEventMonitor.class));
	}
}
