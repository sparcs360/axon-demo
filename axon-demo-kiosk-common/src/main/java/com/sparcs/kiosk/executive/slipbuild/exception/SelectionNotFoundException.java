package com.sparcs.kiosk.executive.slipbuild.exception;

public class SelectionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SelectionNotFoundException(String selectionId) {
		super(String.format("Selection #%s does not exist", selectionId));
	}
}
