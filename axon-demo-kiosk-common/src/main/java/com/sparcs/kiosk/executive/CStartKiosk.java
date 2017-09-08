package com.sparcs.kiosk.executive;

import com.sparcs.kiosk.ISystemCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class CStartKiosk implements ISystemCommand {

	private final String kioskId;
}
