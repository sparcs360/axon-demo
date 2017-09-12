package com.sparcs.kiosk.executive.account;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper=true)
public class ECashDeposited extends EBalanceIncreased {

	protected ECashDeposited() {
		super();
	}
	
	@Builder
	private ECashDeposited(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
