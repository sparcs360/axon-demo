package com.sparcs.kiosk.executive.slipbuild;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ESelectionAdded {

	private final String kioskId;
	private final String selectionId;
	private final String name;
	private final int numerator;
	private final int denominator;
}