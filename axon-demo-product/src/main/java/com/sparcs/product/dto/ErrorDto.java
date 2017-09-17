package com.sparcs.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDto {

	private final String message;
	
	@JsonCreator
	public ErrorDto(@JsonProperty("message") String message) {
		
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
