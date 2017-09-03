package com.sparcs.kiosk.executive;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public abstract class ExecutiveCommand {

	@TargetAggregateIdentifier
	protected final String kioskId;
}
