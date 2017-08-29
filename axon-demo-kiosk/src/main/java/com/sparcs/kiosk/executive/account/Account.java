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
	public void handle(CInsertNote cmd) {
		
		LOG.trace("handle(cmd={})", cmd);
		if (cmd.getAmount() <= 0) {
			throw new InvalidGBPAmountException();
		}
		AggregateLifecycle.apply(new ENoteInserted(cmd.getKioskId(), balance + cmd.getAmount(), cmd.getAmount()));
	}
	
	@EventSourcingHandler
	public void on(EBalanceChanged event) {
		
		LOG.trace("on(event={})", event);
		balance = event.getBalance();
	}

    public synchronized int getBalance() {
    	return balance;
    }
    
	public synchronized void setBalance(int balance) {
		this.balance = balance;
	}
}
