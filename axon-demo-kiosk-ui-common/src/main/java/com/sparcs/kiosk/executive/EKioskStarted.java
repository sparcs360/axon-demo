package com.sparcs.kiosk.executive;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EKioskStarted {

	private final String kioskId;
}
