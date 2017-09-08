package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.sparcs.kiosk.IEndUserCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CAddSelection implements IEndUserCommand {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final String selectionId;
}
