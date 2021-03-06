package com.sparcs.kiosk.executive.slipbuild;

import com.sparcs.kiosk.IKioskEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ESelectionRemoved implements IKioskEvent {

	private final String kioskId;
	private final String selectionId;
	private final String name;
	private final int numerator;
	private final int denominator;
}
