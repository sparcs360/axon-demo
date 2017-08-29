package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/account")
public class AccountController {

	private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final CommandGateway commandGateway;
    private final BalanceTracker balanceTracker;

    @Autowired
    public AccountController(CommandGateway commandGateway, BalanceTracker balanceTracker) {

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
        commandGateway.send(message.getPayload());
    }
}
