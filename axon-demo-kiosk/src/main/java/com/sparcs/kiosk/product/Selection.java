package com.sparcs.kiosk.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Selection {

	private final String id;
	private final String name;
	private final int numerator;
	private final int denominator;
}
