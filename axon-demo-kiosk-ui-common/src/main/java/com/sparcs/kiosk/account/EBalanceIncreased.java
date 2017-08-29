package com.sparcs.kiosk.account;

import lombok.ToString;

@ToString(callSuper=true)
public abstract class EBalanceIncreased extends EBalanceChanged {

	protected EBalanceIncreased() {
		super();
	}
	
	protected EBalanceIncreased(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
