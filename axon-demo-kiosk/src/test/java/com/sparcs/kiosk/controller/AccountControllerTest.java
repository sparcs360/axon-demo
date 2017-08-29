package com.sparcs.kiosk.controller;

import static org.mockito.Mockito.verify;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.sparcs.kiosk.executive.account.AccountController;
import com.sparcs.kiosk.executive.account.BalanceTracker;
import com.sparcs.kiosk.executive.account.CInsertNote;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

	@Mock
    private CommandGateway mockCommandGateway;
	@Mock
    private BalanceTracker mockBalanceTracker;
	
	private AccountController unitUnderTest;
	
	@Before
	public void setup() {
		
		unitUnderTest = new AccountController(mockCommandGateway, mockBalanceTracker);
	}
	
	@Test
	public void when_getBalance_then_ShouldDelegateToBalanceTracker() {
		
		unitUnderTest.getBalance(null);
		
		verify(mockBalanceTracker).getBalance();
	}
	
	@Test
	public void when_depositCash_then_ShouldSendCommand() {
		
		CInsertNote command = CInsertNote.builder().kioskId("KIOSK_ID").amount(337).build();
		Message<CInsertNote> message = MessageBuilder.withPayload(command).build();
		
		unitUnderTest.depositCash(message);
		
		verify(mockCommandGateway).send(command);
	}
}
