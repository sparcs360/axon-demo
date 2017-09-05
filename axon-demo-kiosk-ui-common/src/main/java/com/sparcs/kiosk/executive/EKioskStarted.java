package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.KioskEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EKioskStarted implements KioskEvent {

	private final String kioskId;
}
