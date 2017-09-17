package com.sparcs.product.dto;

import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionDto {

	private final List<EventDto> events;

	public Optional<EventDto> findEvent(String id) {
		
		return events.stream()
				.filter(e -> e.getId().equals(id))
				.findFirst();
	}

	public Optional<SelectionDto> findSelection(String selectionId) {
		
		return events.stream()
				.flatMap(e -> e.getSelections().stream())
				.filter(s -> s.getId().equals(selectionId))
				.findFirst();
	}
}
