package com.sparcs.kiosk.slipbuild;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ESelectionRemoved {

	private final String kioskId;
	private final String selectionId;
	private final String name;
	private final int numerator;
	private final int denominator;
}
