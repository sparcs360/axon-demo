package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.IKioskEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EKioskReset implements IKioskEvent {

	private final String kioskId;
	private final String reason;
}
