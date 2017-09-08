package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.IKioskEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EKioskReset implements IKioskEvent {

	private final String kioskId;
	private final String reason;
}
