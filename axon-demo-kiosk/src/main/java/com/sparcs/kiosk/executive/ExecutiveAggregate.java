package com.sparcs.kiosk.executive;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sparcs.kiosk.executive.EKioskReset;
import com.sparcs.kiosk.executive.EKioskStarted;
import com.sparcs.kiosk.executive.account.Account;
import com.sparcs.kiosk.executive.account.EBalanceReset;
import com.sparcs.kiosk.executive.slipbuild.PotentialSlip;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class ExecutiveAggregate {

	private static final Logger LOG = LoggerFactory.getLogger(ExecutiveAggregate.class);
	
	@AggregateIdentifier
	private String kioskId;
	
	@AggregateMember
	private Account account;
	@AggregateMember
	private PotentialSlip potentialSlip;
	
	@CommandHandler
	public ExecutiveAggregate(CStartKiosk cmd) {
		
		LOG.trace("ctor(cmd={})", cmd);

		AggregateLifecycle.apply(EKioskStarted.builder().kioskId(cmd.getKioskId()).build());
	}
	
	@EventSourcingHandler
	public void on(EKioskStarted event) {
		
		LOG.trace("on(event={})", event);

		this.kioskId = event.getKioskId();
		this.account = Account.INSTANCE;
		this.potentialSlip = PotentialSlip.builder().build();
	}

	@CommandHandler
	public void handle(CResetKiosk cmd) {
		
		LOG.trace("handle(cmd={})", cmd);
		AggregateLifecycle
			.apply(new EKioskReset(cmd.getKioskId(), cmd.getReason()))
			.andThenApply(() -> new EBalanceReset(cmd.getKioskId(), 0, -account.getBalance()));
	}

	@EventSourcingHandler
	public void on(EKioskReset event) {
		
		LOG.trace("on(event={})", event);		
	}
}
