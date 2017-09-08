package com.sparcs.kiosk.executive;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.sparcs.kiosk.ICounterToKioskCommand;
import com.sparcs.kiosk.ISystemCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CResetKiosk implements ISystemCommand, ICounterToKioskCommand {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final String reason;
}
