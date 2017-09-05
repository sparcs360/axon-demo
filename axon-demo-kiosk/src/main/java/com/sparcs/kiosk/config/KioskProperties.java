package com.sparcs.kiosk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KioskProperties {

	private final String shopId;
	private final String index;
	private final String kioskId;
	private final String amqpHostName;
	private final String amqpKioskEventsOutExchangeName;
	private final String amqpKioskCommandsInExchangeName;
	private final String amqpKioskCommandsInQueueName;
	
	@Autowired
	KioskProperties(
			@Value("${kiosk.shop.id}") String shopId,
			@Value("${kiosk.index}") String index,
			@Value("${kiosk.amqp.host-name}") String amqpHostName,
			@Value("${kiosk.amqp.kiosk-events-out.exchange-name}") String amqpKioskEventsOutExchangeName,
			@Value("${kiosk.amqp.kiosk-commands-in.exchange-name}") String amqpKioskCommandsInExchangeName,
			@Value("${kiosk.amqp.kiosk-commands-in.queue-name}") String amqpKioskCommandsInQueueName) {
		
		this.shopId = shopId;
		this.index = index;
		this.kioskId = String.format("%s-%s", shopId, index);
		this.amqpHostName = amqpHostName;
		this.amqpKioskEventsOutExchangeName = amqpKioskEventsOutExchangeName;
		this.amqpKioskCommandsInExchangeName = amqpKioskCommandsInExchangeName;
		this.amqpKioskCommandsInQueueName = amqpKioskCommandsInQueueName;
	}

	public String getShopId() {
		return shopId;
	}

	public String getIndex() {
		return index;
	}

	public String getKioskId() {
		return kioskId;
	}

	public String getAmqpHostName() {
		return amqpHostName;
	}

	public String getAmqpKioskEventsOutExchangeName() {
		return amqpKioskEventsOutExchangeName;
	}

	public String getAmqpKioskCommandsInExchangeName() {
		return amqpKioskCommandsInExchangeName;
	}

	public String getAmqpKioskCommandsInQueueName() {
		return amqpKioskCommandsInQueueName;
	}
}
