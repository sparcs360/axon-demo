package com.sparcs.product.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {

	private final String id;
	private final String name;
	private final List<SelectionDto> selections;
	
}
