package com.sparcs.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectionDto {

	private final String selectionId;
	private final String name;
	private final int numerator;
	private final int denominator;
}
