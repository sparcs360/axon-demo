package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.KioskEvent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EKioskReset implements KioskEvent {

	private final String kioskId;
	private final String reason;
}
