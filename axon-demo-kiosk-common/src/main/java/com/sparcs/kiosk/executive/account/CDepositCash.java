package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.sparcs.kiosk.EndUserCommand;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class CDepositCash implements EndUserCommand {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final int amount;
}
