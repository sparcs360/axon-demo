package com.sparcs.kiosk.executive.account;

import com.sparcs.kiosk.IKioskEvent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public abstract class EBalanceChanged implements IKioskEvent {

	private final String kioskId;
	private final int balance;
	private final int amount;
	
	protected EBalanceChanged() {
		kioskId = null;
		balance = 0;
		amount = 0;
	}
}
