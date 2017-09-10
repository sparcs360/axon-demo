package com.sparcs.kiosk.executive.account;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

	@Mock
    private BalanceTracker mockBalanceTracker;
	
	private AccountController unitUnderTest;
	
	@Before
	public void setup() {

		unitUnderTest = new AccountController(mockBalanceTracker);
	}
	
	@Test
	public void when_getBalance_then_ShouldDelegateToBalanceTracker() {
		
		unitUnderTest.getBalance(null);
		
		verify(mockBalanceTracker).getBalance();
	}
}
