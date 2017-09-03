package com.sparcs.kiosk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KioskProperties {

	private final String kioskId;
	private final String amqpHostName;
	private final String amqpShopExchangeName;
	private final String amqpShopQueueName;
	private final String amqpEventbusExchangeName;
	
	@Autowired
	KioskProperties(
			@Value("${kiosk.id}") String kioskId,
			@Value("${kiosk.amqp.host-name}") String amqpHostName,
			@Value("${kiosk.amqp.eventbus.exchange-name}") String amqpEventbusExchangeName,
			@Value("${kiosk.amqp.shop.exchange-name}") String amqpShopExchangeName,
			@Value("${kiosk.amqp.shop.queue-name}") String amqpShopQueueName) {
		
		this.kioskId = kioskId;
		this.amqpHostName = amqpHostName;
		this.amqpEventbusExchangeName = amqpEventbusExchangeName;
		this.amqpShopExchangeName = amqpShopExchangeName;
		this.amqpShopQueueName = amqpShopQueueName;
	}

	public String getKioskId() {
		return kioskId;
	}

	public String getAmqpHostName() {
		return amqpHostName;
	}

	public String getAmqpEventbusExchangeName() {
		return amqpEventbusExchangeName;
	}

	public String getAmqpShopExchangeName() {
		return amqpShopExchangeName;
	}

	public String getAmqpShopQueueName() {
		return amqpShopQueueName;
	}
}
