package com.sparcs.kiosk.executive.account;

import lombok.ToString;

@ToString(callSuper=true)
public abstract class EBalanceDecreased extends EBalanceChanged {
	
	protected EBalanceDecreased() {
		super();
	}
	
	protected EBalanceDecreased(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
