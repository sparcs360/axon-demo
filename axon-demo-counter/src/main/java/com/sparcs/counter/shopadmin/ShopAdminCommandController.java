package com.sparcs.counter.shopadmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparcs.counter.config.CounterProperties;
import com.sparcs.kiosk.executive.CResetKiosk;

@RestController
public class ShopAdminCommandController {

	private static final Logger LOG = LoggerFactory.getLogger(ShopAdminCommandController.class);

	private final CounterProperties counterProperties;
	private final AmqpTemplate amqpTemplate;
	
	@Autowired
	ShopAdminCommandController(CounterProperties counterProperties, AmqpTemplate amqpTemplate) {
		
		this.counterProperties = counterProperties;
		this.amqpTemplate = amqpTemplate;
	}
	
	@PostMapping("/kiosk/{kioskId}/reset")
	public void resetKiosk(@PathVariable String kioskId, @RequestParam(required=true) String reason) {
		
		LOG.info("resetKiosk(kioskId={}, reason={})", kioskId, reason);

		CResetKiosk command = CResetKiosk.builder().kioskId(kioskId).reason(reason).build();
		amqpTemplate.convertAndSend(counterProperties.getAmqpShopExchangeName(), kioskId, command);
	}
}
