package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CAddSelection {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final String selectionId;
}
