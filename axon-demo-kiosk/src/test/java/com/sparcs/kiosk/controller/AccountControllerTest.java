package com.sparcs.kiosk.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.sparcs.kiosk.config.KioskProperties;
import com.sparcs.kiosk.executive.account.AccountController;
import com.sparcs.kiosk.executive.account.BalanceTracker;
import com.sparcs.kiosk.executive.account.CDepositCash;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

	private static final String KIOSK_ID = "KIOSK_ID";
	
	@Mock
    private KioskProperties mockKioskProperties;
	@Mock
    private CommandGateway mockCommandGateway;
	@Mock
    private BalanceTracker mockBalanceTracker;
	
	private AccountController unitUnderTest;
	
	@Before
	public void setup() {
		
		when(mockKioskProperties.getKioskId()).thenReturn(KIOSK_ID);

		unitUnderTest = new AccountController(mockKioskProperties, mockCommandGateway, mockBalanceTracker);
	}
	
	@Test
	public void when_getBalance_then_ShouldDelegateToBalanceTracker() {
		
		unitUnderTest.getBalance(null);
		
		verify(mockBalanceTracker).getBalance();
	}
	
	@Test
	public void when_depositCash_then_ShouldSendCommand() {
		
		CDepositCash command = CDepositCash.builder().kioskId(KIOSK_ID).amount(337).build();
		Message<CDepositCash> message = MessageBuilder.withPayload(command).build();
		
		unitUnderTest.depositCash(message);
		
		verify(mockCommandGateway).send(command);
	}
}
