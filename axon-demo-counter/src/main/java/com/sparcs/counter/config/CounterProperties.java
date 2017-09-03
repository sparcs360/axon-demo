package com.sparcs.counter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CounterProperties {

	private final String shopId;
	private final String amqpHostName;
	private final String amqpEventbusExchangeName;
	private final String amqpEventbusQueueName;
	private final String amqpShopExchangeName;
	
	public CounterProperties(
			@Value("${counter.shop.id}") String shopId,
			@Value("${counter.amqp.host-name}") String amqpHostName,
			@Value("${counter.amqp.eventbus.exchange-name}") String amqpEventbusExchangeName,
			@Value("${counter.amqp.eventbus.queue-name}") String amqpEventbusQueueName,
			@Value("${counter.amqp.shop.exchange-name}") String amqpShopExchangeName) {

		this.shopId = shopId;
		this.amqpHostName = amqpHostName;
		this.amqpEventbusExchangeName = amqpEventbusExchangeName;
		this.amqpEventbusQueueName = amqpEventbusQueueName;
		this.amqpShopExchangeName = amqpShopExchangeName;
	}

	public String getShopId() {
		return shopId;
	}

	public String getAmqpHostName() {
		return amqpHostName;
	}

	public String getAmqpEventbusExchangeName() {
		return amqpEventbusExchangeName;
	}

	public String getAmqpEventbusQueueName() {
		return amqpEventbusQueueName;
	}

	public String getAmqpShopExchangeName() {
		return amqpShopExchangeName;
	}
}
