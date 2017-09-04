package com.sparcs.instrumentation;

import org.axonframework.messaging.Message;
import org.axonframework.monitoring.MessageMonitor;
import org.axonframework.monitoring.MessageMonitor.MonitorCallback;
import org.slf4j.Logger;

public abstract class LoggingMessageMonitor implements MessageMonitor<Message<?>>, MonitorCallback {

	private final Logger LOG;
	
	protected LoggingMessageMonitor(Logger logger) {
		LOG = logger;
	}
	
	@Override
	public MonitorCallback onMessageIngested(Message<?> message) {

		LOG.trace("onMessageIngested(payload={}, id={})",
				message.getPayload(), message.getIdentifier());
		return this;
	}

	@Override
	public void reportSuccess() {

		LOG.trace("reportSuccess");
	}

	@Override
	public void reportFailure(Throwable cause) {

		LOG.error("reportFailure", cause);
	}

	@Override
	public void reportIgnored() {

		LOG.warn("reportIgnored");
	}
}
