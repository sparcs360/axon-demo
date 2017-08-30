package com.sparcs.kiosk.executive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CStartKiosk {

	private final String kioskId;
}
