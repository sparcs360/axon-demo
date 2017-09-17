package com.sparcs.product;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sparcs.product.dto.CompetitionDto;
import com.sparcs.product.dto.ErrorDto;
import com.sparcs.product.dto.EventDto;
import com.sparcs.product.dto.SelectionDto;

@RestController
public class ProductController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	@GetMapping("/competition/{competitionId}")
	public ResponseEntity<?> getCompetitionById(@PathVariable String competitionId) {

		LOG.debug("getCompetitionById(competitionId={})", competitionId);

		if (competitionId.equals("TWWC2017")) {
			return new ResponseEntity<CompetitionDto>(TWWC2017, HttpStatus.OK);
		}

		ErrorDto error = new ErrorDto(String.format("Competition with Id [%s] not found", competitionId));
		return new ResponseEntity<ErrorDto>(error, HttpStatus.NOT_FOUND);
	}

	private static CompetitionDto TWWC2017 = CompetitionDto.builder()
		.competitionId("TWWC2017")
		.name("Tiddly-Winks World Championshop 2017")
		.events(Arrays.asList(
			EventDto.builder()
				.eventId("E1")
				.name("Barnsley vs Sheffield")
				.selections(Arrays.asList(
					SelectionDto.builder()
						.selectionId("S1.1")
						.name("Barnsley")
						.numerator(7)
						.denominator(6)
						.build(),
					SelectionDto.builder()
						.selectionId("S1.2")
						.name("Draw")
						.numerator(2)
						.denominator(1)
						.build(),
					SelectionDto.builder()
						.selectionId("S1.3")
						.name("Sheffield")
						.numerator(3)
						.denominator(1)
						.build()
				))
				.build(),
			EventDto.builder()
				.eventId("E2")
				.name("Leeds vs Manchester")
				.selections(Arrays.asList(
					SelectionDto.builder()
						.selectionId("S2.1")
						.name("Leeds")
						.numerator(3)
						.denominator(2)
						.build(),
					SelectionDto.builder()
						.selectionId("S2.2")
						.name("Draw")
						.numerator(11)
						.denominator(10)
						.build(),
					SelectionDto.builder()
						.selectionId("S2.3")
						.name("Manchester")
						.numerator(2)
						.denominator(1)
						.build()
				))
				.build()
		))
		.build();
}
