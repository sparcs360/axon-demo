package com.sparcs.kiosk.executive;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CResetKiosk {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final String reason;
}
