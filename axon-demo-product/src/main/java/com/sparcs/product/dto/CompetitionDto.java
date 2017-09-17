package com.sparcs.product.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompetitionDto {

	private final String name;
	private final List<EventDto> events;
}
