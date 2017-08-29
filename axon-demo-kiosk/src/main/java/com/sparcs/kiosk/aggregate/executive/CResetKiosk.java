package com.sparcs.kiosk.aggregate.executive;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CResetKiosk {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final String reason;
}
