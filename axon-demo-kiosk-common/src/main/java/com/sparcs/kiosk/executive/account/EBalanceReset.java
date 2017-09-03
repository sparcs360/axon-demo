package com.sparcs.kiosk.executive.account;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper=true)
@Builder
public class EBalanceReset extends EBalanceDecreased {

	protected EBalanceReset() {
		super();
	}

	public EBalanceReset(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
