package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/account")
public class AccountController {

	private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

	private final String kioskId;
    private final CommandGateway commandGateway;
    private final BalanceTracker balanceTracker;

    @Autowired
    public AccountController(@Value("${kiosk.id}") String kioskId, CommandGateway commandGateway, BalanceTracker balanceTracker) {

    	this.kioskId = kioskId;
        this.commandGateway = commandGateway;
        this.balanceTracker = balanceTracker;
    }

    @MessageMapping("/balance/get")
    @SendTo("/topic/account/balance")
    public int getBalance(Message<Object> message) {

    	LOG.debug("getBalance({})", message);
    	return balanceTracker.getBalance();
    }

    @MessageMapping("/deposit/cash")
    public void depositCash(Message<CInsertNote> message) {

    	LOG.debug("depositCash({})", message);
        CInsertNote commandWithIdentifier = message.getPayload().toBuilder().kioskId(kioskId).build();
		commandGateway.send(commandWithIdentifier);
    }
}
