package com.sparcs.kiosk.executive.slipbuild;

import org.axonframework.commandhandling.model.EntityId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PotentialSelection {

	@EntityId
	private final String selectionId;
	private final String name;
	private final int numerator;
	private final int denominator;
	private int stake;
	
	public static PotentialSelectionBuilder builderFrom(ESelectionAdded event) {

		return builder()
				.selectionId(event.getSelectionId())
				.name(event.getName())
				.denominator(event.getDenominator())
				.numerator(event.getNumerator())
				.stake(0);
	}
}
