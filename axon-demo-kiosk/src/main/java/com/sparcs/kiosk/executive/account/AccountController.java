package com.sparcs.kiosk.executive.account;

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

    private final BalanceTracker balanceTracker;

    @Autowired
    public AccountController(BalanceTracker balanceTracker) {

        this.balanceTracker = balanceTracker;
    }

    @MessageMapping("/balance/get")
    @SendTo(BalanceTracker.TOPIC_NAME)
    public int getBalance(Message<Object> message) {

    	LOG.debug("getBalance({})", message);
    	return balanceTracker.getBalance();
    }
}
