package com.sparcs.kiosk.executive.slipbuild;

import com.sparcs.kiosk.IKioskEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EPotentialSlipCleared implements IKioskEvent {

	private final String kioskId;
}
