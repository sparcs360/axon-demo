package com.sparcs.kiosk.executive.account;

import lombok.Builder;
import lombok.ToString;

@ToString(callSuper=true)
@Builder
public class ENoteInserted extends EBalanceIncreased {

	protected ENoteInserted() {
		super();
	}
	
	public ENoteInserted(String kioskId, int balance, int amount) {
		super(kioskId, balance, amount);
	}
}
