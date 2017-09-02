package com.sparcs.counter.shopadmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparcs.counter.config.ShopCommandsConfig;
import com.sparcs.kiosk.executive.CResetKiosk;

@RestController
public class ShopAdminCommandController {

	private static final Logger LOG = LoggerFactory.getLogger(ShopAdminCommandController.class);

	private final AmqpTemplate amqpTemplate;
	private final String exchangeName;
	
	@Autowired
	ShopAdminCommandController(
			@Qualifier("shopCommandAmqpTemplate") AmqpTemplate amqpTemplate,
			@Value("${" + ShopCommandsConfig.PROPERTY_PATH + ".exchange-name}") String exchangeName) {
		
		this.amqpTemplate = amqpTemplate;
		this.exchangeName = exchangeName;
	}
	
	@PostMapping("/kiosk/{kioskId}/reset")
	public void resetKiosk(@PathVariable String kioskId, @RequestParam(required=true) String reason) {
		
		LOG.info("resetKiosk(kioskId={}, reason={}) <- exchangeName={}", kioskId, reason, exchangeName);

		CResetKiosk command = CResetKiosk.builder().kioskId(kioskId).reason(reason).build();
		amqpTemplate.convertAndSend(kioskId, command);
	}
}
