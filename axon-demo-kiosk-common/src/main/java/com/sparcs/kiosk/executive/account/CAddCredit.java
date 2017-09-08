package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.sparcs.kiosk.ICounterToKioskCommand;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class CAddCredit implements ICounterToKioskCommand {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final int amount;
}
