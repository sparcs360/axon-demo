package com.sparcs.kiosk.executive.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparcs.kiosk.IEndUserCommand;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class CDepositCash implements IEndUserCommand {

	@TargetAggregateIdentifier
	private final String kioskId;

	@JsonProperty(required=true)
	private final int amount;
}
