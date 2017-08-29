package com.sparcs.kiosk.aggregate.executive.slipbuild;

import org.axonframework.commandhandling.model.EntityId;

import com.sparcs.kiosk.slipbuild.ESelectionAdded;

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
