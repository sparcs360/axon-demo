package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class CInsertNote {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final int amount;
}
