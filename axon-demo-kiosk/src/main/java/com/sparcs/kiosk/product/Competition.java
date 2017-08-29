package com.sparcs.kiosk.product;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Competition {

	public static Competition TWWC2017 = Competition.builder()
			.events(Arrays.asList(
				Event.builder()
					.id("E1")
					.name("Barnsley vs Sheffield")
					.selections(Arrays.asList(
						Selection.builder()
							.id("S1.1")
							.name("Barnsley")
							.numerator(7)
							.denominator(6)
							.build(),
						Selection.builder()
							.id("S1.2")
							.name("Draw")
							.numerator(2)
							.denominator(1)
							.build(),
						Selection.builder()
							.id("S1.3")
							.name("Sheffield")
							.numerator(3)
							.denominator(1)
							.build()
					))
					.build(),
				Event.builder()
					.id("E2")
					.name("Leeds vs Manchester")
					.selections(Arrays.asList(
						Selection.builder()
							.id("S2.1")
							.name("Leeds")
							.numerator(3)
							.denominator(2)
							.build(),
						Selection.builder()
							.id("S2.2")
							.name("Draw")
							.numerator(11)
							.denominator(10)
							.build(),
						Selection.builder()
							.id("S2.3")
							.name("Manchester")
							.numerator(2)
							.denominator(1)
							.build()
					))
					.build()
			))
			.build();

	private final List<Event> events;

	public Optional<Event> findEvent(String id) {
		
		return events.stream()
				.filter(e -> e.getId().equals(id))
				.findFirst();
	}

	public Optional<Selection> findSelection(String selectionId) {
		
		return events.stream()
				.flatMap(e -> e.getSelections().stream())
				.filter(s -> s.getId().equals(selectionId))
				.findFirst();
	}
}
