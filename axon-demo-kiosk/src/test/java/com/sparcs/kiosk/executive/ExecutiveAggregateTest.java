package com.sparcs.kiosk.executive;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.sparcs.kiosk.executive.account.CAddCredit;
import com.sparcs.kiosk.executive.account.CDepositCash;
import com.sparcs.kiosk.executive.account.CRemoveCredit;
import com.sparcs.kiosk.executive.account.EBalanceReset;
import com.sparcs.kiosk.executive.account.ECashDeposited;
import com.sparcs.kiosk.executive.account.ECreditAdded;
import com.sparcs.kiosk.executive.account.ECreditRemoved;
import com.sparcs.kiosk.executive.account.exception.InvalidGBPAmountException;
import com.sparcs.kiosk.executive.slipbuild.CAddSelection;
import com.sparcs.kiosk.executive.slipbuild.EPotentialSlipCleared;
import com.sparcs.kiosk.executive.slipbuild.ESelectionAdded;
import com.sparcs.kiosk.executive.slipbuild.exception.SelectionAlreadyOnSlipException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("DisableAmqp") // 'Turn off' communication with the RabbitMQ server
public class ExecutiveAggregateTest {

	private static final String KIOSK_ID = "KIOSK_ID";
	private static final String SELECTION_ID = "S1.1";
	private static final String SELECTION_NAME = "Barnsley";
	private static final int NUMERATOR = 7;
	private static final int DENOMINATOR = 6;

	private FixtureConfiguration<ExecutiveAggregate> fixture;
	
	private CStartKiosk aStartKioskCommand;
	private EKioskStarted aKioskStartedEvent;

	private CResetKiosk aResetKioskCommand;
	private EBalanceReset aBalanceResetEvent;
	private EPotentialSlipCleared aPotentialSlipClearedEvent;
	private EKioskReset aKioskResetEvent;

	private CAddSelection anAddSelectionCommand;
	private ESelectionAdded aSelectionAddedEvent;

	@Before
	public void setup() {

		aStartKioskCommand = CStartKiosk.builder().kioskId(KIOSK_ID).build();
		aKioskStartedEvent = EKioskStarted.builder().kioskId(KIOSK_ID).build();

		aResetKioskCommand = CResetKiosk.builder().kioskId(KIOSK_ID).reason("REASON").build();
		aBalanceResetEvent = EBalanceReset.builder().kioskId(KIOSK_ID).amount(0).build();
		aPotentialSlipClearedEvent = EPotentialSlipCleared.builder().kioskId(KIOSK_ID).build();
		aKioskResetEvent = EKioskReset.builder().kioskId(KIOSK_ID).reason("REASON").build();

		anAddSelectionCommand = CAddSelection.builder().kioskId(KIOSK_ID).selectionId(SELECTION_ID).build();
		aSelectionAddedEvent = ESelectionAdded.builder()
				.kioskId(KIOSK_ID)
				.selectionId(SELECTION_ID)
				.name(SELECTION_NAME)
				.numerator(NUMERATOR)
				.denominator(DENOMINATOR)
				.build();

		fixture = new AggregateTestFixture<>(ExecutiveAggregate.class);
	}

	@Test
	public void when_StartSsbtCommand_then_ShouldExpectEvents() {

		fixture
			.givenNoPriorActivity()
			.when(aStartKioskCommand)
			.expectEvents(aKioskStartedEvent)
			.expectReturnValue(KIOSK_ID);
	}

	@Test
	public void when_ResetSsbtCommand_then_ShouldExpectEvents() {

		fixture
			.given(aKioskStartedEvent)
			.when(aResetKioskCommand)
			.expectEvents(aBalanceResetEvent, aPotentialSlipClearedEvent, aKioskResetEvent);
	}

	@Test
	public void given_SsbtNotStarted_when_InsertNoteCommand_then_ShouldThrowException() {

		fixture
			.givenNoPriorActivity()
			.when(aDepositCashCommandFor(2000))
			.expectException(AggregateNotFoundException.class);
	}

	@Test
	public void when_DepositCashCommand_then_ShouldExpectEvents() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(aDepositCashCommandFor(2000))
			.expectEvents(aCashDepositedEventBringingBalanceTo(2000, 2000));
	}

	@Test
	public void when_DepositCashCommand_and_AmountIsZero_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(aDepositCashCommandFor(0))
			.expectNoEvents()
			.expectException(InvalidGBPAmountException.class);
	}

	@Test
	public void when_DepositCashCommand_and_AmountIsNegative_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(aDepositCashCommandFor(-500))
			.expectNoEvents()
			.expectException(InvalidGBPAmountException.class);
	}

	@Test
	public void when_AddCreditCommand_then_ShouldExpectEvents() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(anAddCreditCommandFor(2000))
			.expectEvents(aCreditAddedEventBringingBalanceTo(2000, 2000));
	}

	@Test
	public void when_AddCreditCommand_and_AmountIsZero_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(anAddCreditCommandFor(0))
			.expectNoEvents()
			.expectException(InvalidGBPAmountException.class);
	}

	@Test
	public void when_AddCreditCommand_and_AmountIsNegative_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(anAddCreditCommandFor(-500))
			.expectNoEvents()
			.expectException(InvalidGBPAmountException.class);
	}

	@Test
	public void when_RemoveCreditCommand_then_ShouldExpectEvents() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent, aCreditAddedEventBringingBalanceTo(2000, 2000))
			.when(aRemoveCreditCommandFor(1000))
			.expectEvents(aCreditRemovedEventBringingBalanceTo(1000, 1000));
	}

	@Test
	public void when_RemoveCreditCommand_and_AmountIsZero_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(aRemoveCreditCommandFor(0))
			.expectNoEvents()
			.expectException(InvalidGBPAmountException.class);
	}

	@Test
	public void when_RemoveCreditCommand_and_AmountIsNegative_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(aRemoveCreditCommandFor(-500))
			.expectNoEvents()
			.expectException(InvalidGBPAmountException.class);
	}

	@Test
	public void when_RemoveCreditCommand_and_AmountIsGreaterThanBalance_then_ShouldBringBalanceToZero() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent, aCreditAddedEventBringingBalanceTo(500, 500))
			.when(aRemoveCreditCommandFor(1000))
			.expectEvents(aCreditRemovedEventBringingBalanceTo(500, 0));
	}

	@Test
	public void when_AddSelectionCommand_then_ShouldPublishEvent() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent)
			.when(anAddSelectionCommand)
			.expectEvents(aSelectionAddedEvent);
	}

	@Test
	public void when_AddSelectionCommand_and_SelectionAlreadyAdded_then_ShouldThrowException() {

		fixture
			.given(aKioskStartedEvent, aBalanceResetEvent, aKioskResetEvent, aSelectionAddedEvent)
			.when(anAddSelectionCommand)
			.expectNoEvents()
			.expectException(SelectionAlreadyOnSlipException.class);
	}

	private CDepositCash aDepositCashCommandFor(int amount) {
		
		return CDepositCash.builder()
				.kioskId(KIOSK_ID)
				.amount(amount)
				.build();
	}
	
	private ECashDeposited aCashDepositedEventBringingBalanceTo(int amount, int balance) {
		
		return ECashDeposited.builder()
				.kioskId(KIOSK_ID)
				.balance(balance)
				.amount(amount)
				.build();
	}

	private CAddCredit anAddCreditCommandFor(int amount) {
		
		return CAddCredit.builder()
				.kioskId(KIOSK_ID)
				.amount(amount)
				.build();
	}
	
	private ECreditAdded aCreditAddedEventBringingBalanceTo(int amount, int balance) {
		
		return ECreditAdded.builder()
				.kioskId(KIOSK_ID)
				.balance(balance)
				.amount(amount)
				.build();
	}

	private CRemoveCredit aRemoveCreditCommandFor(int amount) {
		
		return CRemoveCredit.builder()
				.kioskId(KIOSK_ID)
				.amount(amount)
				.build();
	}
	
	private ECreditRemoved aCreditRemovedEventBringingBalanceTo(int amount, int balance) {
		
		return ECreditRemoved.builder()
				.kioskId(KIOSK_ID)
				.balance(balance)
				.amount(amount)
				.build();
	}
}
