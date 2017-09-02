package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.SystemCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CStartKiosk implements SystemCommand {

	private final String kioskId;
}
