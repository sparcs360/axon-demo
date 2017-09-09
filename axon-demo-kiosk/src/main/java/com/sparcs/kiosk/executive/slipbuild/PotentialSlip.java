package com.sparcs.kiosk.executive.slipbuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sparcs.kiosk.executive.slipbuild.exception.SelectionAlreadyOnSlipException;
import com.sparcs.kiosk.executive.slipbuild.exception.SelectionNotFoundException;
import com.sparcs.kiosk.product.Competition;
import com.sparcs.kiosk.product.Selection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PotentialSlip {

	private static final Logger LOG = LoggerFactory.getLogger(PotentialSlip.class);
	
	@AggregateMember
	@Builder.Default
	private List<PotentialSelection> selections = new ArrayList<>();
	
	@CommandHandler
	public void handle(CAddSelection cmd) {
		
		LOG.trace("handle(cmd={})", cmd);

		if (containsSelection(cmd.getSelectionId())) {
			throw new SelectionAlreadyOnSlipException(cmd.getSelectionId());
		}

		Optional<Selection> possibleSelection = Competition.TWWC2017.findSelection(cmd.getSelectionId());
		if(!possibleSelection.isPresent()) {
			throw new SelectionNotFoundException(cmd.getSelectionId());
		}
		
		Selection selection = possibleSelection.get();
		ESelectionAdded event = ESelectionAdded.builder()
				.kioskId(cmd.getKioskId())
				.selectionId(cmd.getSelectionId())
				.name(selection.getName())
				.denominator(selection.getDenominator())
				.numerator(selection.getNumerator())
				.build();
		AggregateLifecycle.apply(event);
	}

	@CommandHandler
	public void handle(CRemoveSelection cmd) {
		
		LOG.trace("handle(cmd={})", cmd);

		Optional<PotentialSelection> possibleSelection = selections.stream().filter(s -> s.getSelectionId().equals(cmd.getSelectionId())).findFirst();
		if (possibleSelection.isPresent()) {
			
			PotentialSelection selection = possibleSelection.get();
			ESelectionRemoved event = ESelectionRemoved.builder()
					.kioskId(cmd.getKioskId())
					.selectionId(cmd.getSelectionId())
					.name(selection.getName())
					.denominator(selection.getDenominator())
					.numerator(selection.getNumerator())
					.build();
			AggregateLifecycle.apply(event);
		}
	}

	@EventSourcingHandler
	public void on(ESelectionAdded event) {
		
		LOG.trace("on(event={})", event);
		PotentialSelection selection = PotentialSelection.builderFrom(event).build();
		selections.add(selection);
	}

	@EventSourcingHandler
	public void on(ESelectionRemoved event) {
		
		LOG.trace("on(event={})", event);
		selections.removeIf(s -> s.getSelectionId().equals(event.getSelectionId()));
	}

	public boolean containsSelection(String selectionId) {

		return selections.stream().anyMatch(selection -> selection.getSelectionId().equals(selectionId));
	}
}
