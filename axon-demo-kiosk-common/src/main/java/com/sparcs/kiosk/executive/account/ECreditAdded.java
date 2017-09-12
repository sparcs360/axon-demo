package com.sparcs.kiosk.executive.account;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper=true)
public class ECreditAdded extends EBalanceIncreased {

	protected ECreditAdded() {
		super();
	}
	
	@Builder
	private ECreditAdded(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
