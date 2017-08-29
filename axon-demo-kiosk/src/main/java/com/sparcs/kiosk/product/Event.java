package com.sparcs.kiosk.product;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

	private final String id;
	private final String name;
	private final List<Selection> selections;
	
}
