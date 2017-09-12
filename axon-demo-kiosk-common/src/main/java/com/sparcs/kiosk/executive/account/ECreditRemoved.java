package com.sparcs.kiosk.executive.account;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper=true)
public class ECreditRemoved extends EBalanceDecreased {

	protected ECreditRemoved() {
		super();
	}
	
	@Builder
	private ECreditRemoved(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
