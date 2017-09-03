package com.sparcs.kiosk.executive;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.sparcs.kiosk.ShopAdminCommand;
import com.sparcs.kiosk.SystemCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CResetKiosk implements SystemCommand, ShopAdminCommand {

	@TargetAggregateIdentifier
	private final String kioskId;
	private final String reason;
}
