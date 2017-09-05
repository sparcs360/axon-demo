package com.sparcs.counter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CounterProperties {

	private final String shopId;
	private final String amqpHostName;
	private final String amqpKioskEventsInExchangeName;
	private final String amqpKioskEventsInQueueName;
	private final String amqpKioskCommandsOutExchangeName;
	
	public CounterProperties(
			@Value("${counter.shop.id}") String shopId,
			@Value("${counter.amqp.host-name}") String amqpHostName,
			@Value("${counter.amqp.kiosk-events-in.exchange-name}") String amqpKioskEventsInExchangeName,
			@Value("${counter.amqp.kiosk-events-in.queue-name}") String amqpKioskEventsInQueueName,
			@Value("${counter.amqp.kiosk-commands-out.exchange-name}") String amqpKioskCommandsOutExchangeName) {

		this.shopId = shopId;
		this.amqpHostName = amqpHostName;
		this.amqpKioskEventsInExchangeName = amqpKioskEventsInExchangeName;
		this.amqpKioskEventsInQueueName = amqpKioskEventsInQueueName;
		this.amqpKioskCommandsOutExchangeName = amqpKioskCommandsOutExchangeName;
	}

	public String getShopId() {
		return shopId;
	}

	public String getAmqpHostName() {
		return amqpHostName;
	}

	public String getAmqpKioskEventsInExchangeName() {
		return amqpKioskEventsInExchangeName;
	}

	public String getAmqpKioskEventsInQueueName() {
		return amqpKioskEventsInQueueName;
	}

	public String getAmqpKioskCommandsOutExchangeName() {
		return amqpKioskCommandsOutExchangeName;
	}
}
