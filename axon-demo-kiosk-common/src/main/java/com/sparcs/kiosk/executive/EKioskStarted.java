package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.IKioskEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EKioskStarted implements IKioskEvent {

	private final String kioskId;
}
