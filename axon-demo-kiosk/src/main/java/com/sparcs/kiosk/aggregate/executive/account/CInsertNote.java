package com.sparcs.kiosk.aggregate.executive.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CInsertNote {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final int amount;
}
