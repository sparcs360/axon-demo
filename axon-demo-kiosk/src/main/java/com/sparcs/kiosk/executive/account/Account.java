package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sparcs.kiosk.executive.account.exception.InvalidGBPAmountException;

public class Account {
	
	private static final Logger LOG = LoggerFactory.getLogger(Account.class);

    public static Account INSTANCE = new Account();

    private int balance;
    
    private Account() {
    }
    
	@CommandHandler
	public void handle(CDepositCash cmd) {
		
		LOG.trace("handle(cmd={})", cmd);
		if (cmd.getAmount() <= 0) {
			throw new InvalidGBPAmountException();
		}
		AggregateLifecycle.apply(new ECashDeposited(cmd.getKioskId(), balance + cmd.getAmount(), cmd.getAmount()));
	}
	
	@CommandHandler
	public void handle(CAddCredit cmd) {
		
		LOG.trace("handle(cmd={})", cmd);
		if (cmd.getAmount() <= 0) {
			throw new InvalidGBPAmountException();
		}
		AggregateLifecycle.apply(new ECreditAdded(cmd.getKioskId(), balance + cmd.getAmount(), cmd.getAmount()));
	}
	
	@CommandHandler
	public void handle(CRemoveCredit cmd) {
		
		LOG.trace("handle(cmd={})", cmd);
		int amountToRemove = cmd.getAmount();
		if (amountToRemove <= 0) {
			throw new InvalidGBPAmountException();
		}
		if (balance < amountToRemove) {
			LOG.info("Amount to remove is more than current balance - reducing balance to zero");
			amountToRemove = balance;
		}
		AggregateLifecycle.apply(new ECreditRemoved(cmd.getKioskId(), balance - amountToRemove, amountToRemove));
	}

	@EventSourcingHandler
	public void on(EBalanceChanged event) {
		
		LOG.trace("on(event={})", event);
		balance = event.getBalance();
	}

    public synchronized int getBalance() {
    	return balance;
    }
}
