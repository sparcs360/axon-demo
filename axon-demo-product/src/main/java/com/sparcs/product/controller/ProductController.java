package com.sparcs.product.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sparcs.product.dto.CompetitionDto;
import com.sparcs.product.dto.EventDto;
import com.sparcs.product.dto.SelectionDto;

@RestController
public class ProductController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	@GetMapping("/competitions/{competitionId}")
	public CompetitionDto getCompetitionById(@PathVariable String competitionId) {

		LOG.debug("getCompetitionById(competitionId={})", competitionId);
		return TWWC2017;
	}

	private static CompetitionDto TWWC2017 = CompetitionDto.builder()
		.events(Arrays.asList(
			EventDto.builder()
				.id("E1")
				.name("Barnsley vs Sheffield")
				.selections(Arrays.asList(
					SelectionDto.builder()
						.id("S1.1")
						.name("Barnsley")
						.numerator(7)
						.denominator(6)
						.build(),
					SelectionDto.builder()
						.id("S1.2")
						.name("Draw")
						.numerator(2)
						.denominator(1)
						.build(),
					SelectionDto.builder()
						.id("S1.3")
						.name("Sheffield")
						.numerator(3)
						.denominator(1)
						.build()
				))
				.build(),
			EventDto.builder()
				.id("E2")
				.name("Leeds vs Manchester")
				.selections(Arrays.asList(
					SelectionDto.builder()
						.id("S2.1")
						.name("Leeds")
						.numerator(3)
						.denominator(2)
						.build(),
					SelectionDto.builder()
						.id("S2.2")
						.name("Draw")
						.numerator(11)
						.denominator(10)
						.build(),
					SelectionDto.builder()
						.id("S2.3")
						.name("Manchester")
						.numerator(2)
						.denominator(1)
						.build()
				))
				.build()
		))
		.build();
}
