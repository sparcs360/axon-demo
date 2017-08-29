package com.sparcs.kiosk.executive.slipbuild.exception;

public class SelectionAlreadyOnSlipException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SelectionAlreadyOnSlipException(String selectionId) {
		super(String.format("Selection #%s has already been added to the slip", selectionId));
	}
}
