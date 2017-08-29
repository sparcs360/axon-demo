package com.sparcs.kiosk.executive;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EKioskReset {

	private final String kioskId;
	private final String reason;
}
