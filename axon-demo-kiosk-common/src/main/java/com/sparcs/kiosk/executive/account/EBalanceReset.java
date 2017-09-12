package com.sparcs.kiosk.executive.account;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper=true)
public class EBalanceReset extends EBalanceDecreased {

	protected EBalanceReset() {
		super();
	}

	@Builder
	private EBalanceReset(String kioskId, int amount) {
		super(kioskId, 0, amount);
	}
}
