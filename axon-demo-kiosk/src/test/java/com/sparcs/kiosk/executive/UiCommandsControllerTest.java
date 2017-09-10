package com.sparcs.kiosk.executive;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sparcs.kiosk.config.KioskProperties;
import com.sparcs.kiosk.executive.account.CDepositCash;
import com.sparcs.kiosk.executive.slipbuild.CClearPotentialSlip;

@RunWith(MockitoJUnitRunner.class)
public class UiCommandsControllerTest {

	private static final String KIOSK_ID = "KIOSK_ID";
	private static final int AMOUNT = 337;

	private UiCommandsController unitUnderTest;

	@Mock
    private KioskProperties mockKioskProperties;
	@Mock
    private CommandGateway mockCommandGateway;

	@Captor
	private ArgumentCaptor<Object> commandCaptor;
	
	@Before
	public void setup() {
		
		when(mockKioskProperties.getKioskId()).thenReturn(KIOSK_ID);

		unitUnderTest = new UiCommandsController(mockKioskProperties, mockCommandGateway);
	}

	@Test(expected = UiCommandsController.InvalidCommandException.class)
	public void when_PayloadIsntValidJson_then_ShouldThrowException() {

		unitUnderTest.sendCommand("not a valid command name", "not a json document");
	}

	@Test(expected = UiCommandsController.InvalidCommandException.class)
	public void when_InvalidCommandNameSent_then_ShouldThrowException() {

		unitUnderTest.sendCommand("not a valid command name", "{}");
	}

	@Test(expected = UiCommandsController.InvalidCommandException.class)
	public void when_PayloadDoesntDeserialiseToTheCommand_then_ShouldThrowException() {

		unitUnderTest.sendCommand("executive.account.CDepositCash", "{}");
	}

	@Test
	public void when_RequestIsValid_then_ShouldDispatchExpectedCommandViaCommandGateway() {

		unitUnderTest.sendCommand("executive.account.CDepositCash", "{\"amount\": " + AMOUNT + "}");

		verify(mockCommandGateway).send(commandCaptor.capture());

		Object actual = commandCaptor.getValue();
		assertThat(actual, is(instanceOf(CDepositCash.class)));
	}

	@Test
	public void when_RequestIsValid_then_ShouldAddKioskId() {

		unitUnderTest.sendCommand("executive.account.CDepositCash", "{\"amount\": " + AMOUNT + "}");

		verify(mockCommandGateway).send(commandCaptor.capture());

		CDepositCash command = (CDepositCash) commandCaptor.getValue();
		assertThat(command.getKioskId(), is(KIOSK_ID));
	}

	@Test
	public void when_RequestIsValid_then_ShouldSetCommandPropertiesFromPayload() {

		unitUnderTest.sendCommand("executive.account.CDepositCash", "{\"amount\": " + AMOUNT + "}");

		verify(mockCommandGateway).send(commandCaptor.capture());

		CDepositCash command = (CDepositCash) commandCaptor.getValue();
		assertThat(command.getAmount(), is(AMOUNT));
	}

	@Test
	public void when_ValidCommandHasNoPayload_and_EmptyObjectProvided_then_ShouldDispatchExpectedCommandViaCommandGateway() {

		unitUnderTest.sendCommand("executive.slipbuild.CClearPotentialSlip", "{}");
		
		verify(mockCommandGateway).send(commandCaptor.capture());

		Object actual = commandCaptor.getValue();
		assertThat(actual, is(instanceOf(CClearPotentialSlip.class)));
	}

	@Test
	public void when_ValidCommandHasNoPayload_and_EmptyStringProvided_then_ShouldDispatchExpectedCommandViaCommandGateway() {

		unitUnderTest.sendCommand("executive.slipbuild.CClearPotentialSlip", "");
		
		verify(mockCommandGateway).send(commandCaptor.capture());

		Object actual = commandCaptor.getValue();
		assertThat(actual, is(instanceOf(CClearPotentialSlip.class)));
	}

	@Test
	public void when_ValidCommandHasNoPayload_and_NullProvided_then_ShouldDispatchExpectedCommandViaCommandGateway() {

		unitUnderTest.sendCommand("executive.slipbuild.CClearPotentialSlip", null);
		
		verify(mockCommandGateway).send(commandCaptor.capture());

		Object actual = commandCaptor.getValue();
		assertThat(actual, is(instanceOf(CClearPotentialSlip.class)));
	}
}
