package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.sparcs.kiosk.ShopAdminCommand;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class CRemoveCredit implements ShopAdminCommand {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final int amount;
}
