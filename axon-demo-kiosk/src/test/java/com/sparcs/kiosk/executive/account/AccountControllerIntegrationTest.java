package com.sparcs.kiosk.executive.account;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Receiptable;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.sparcs.kiosk.Application;
import com.sparcs.kiosk.executive.CResetKiosk;

// Credit for basic WebSocket TestRig: http://rafaelhz.github.io/testing-websockets/

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@ActiveProfiles("DisableAmqp") // 'Turn off' communication with the RabbitMQ server
public class AccountControllerIntegrationTest {

    private static final String KIOSK_ID = "000001-01";

	static final String WEBSOCKET_URI = "http://localhost:8080/kiosk-websocket";
    static final String SEND_COMMAND_DESTINATION = "/kiosk/commands/send";
    static final String CMD_DEPOSIT_CASH = "executive.account.CDepositCash";
    static final String ACCOUNT_SUB_SUMMARY = "/topic/account/balance";

	private static final int AMOUNT = 337;
	
    static final int SECONDS_TO_WAIT_FOR_RESPONSE = 1;

    BlockingQueue<Object> receivedResponses;
    WebSocketStompClient stompClient;
    StompSession session;
    
    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private MessageConverter messageConverter;
    
    @Before
    public void before() throws Exception {
    	
        receivedResponses = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(messageConverter);
    }

    @After
    public void after() {
    	session.disconnect();
    	commandGateway.sendAndWait(CResetKiosk.builder().kioskId(KIOSK_ID).reason("to reset balance").build());
    }

    @Test
    public void when_DepositCash_then_BalanceShouldBeUpdated() throws Exception {

        givenClientConnectedTo(WEBSOCKET_URI);
		givenClientSubscribesTo(ACCOUNT_SUB_SUMMARY, Integer.class);

		whenClientSendsCommand(CMD_DEPOSIT_CASH, "{\"amount\": " + AMOUNT + "}");

		thenShouldReceiveReply(337);
    }

    @Test
    public void when_DepositMoreCash_then_BalanceShouldBeUpdated() throws Exception {

        givenClientConnectedTo(WEBSOCKET_URI);
		givenClientSubscribesTo(ACCOUNT_SUB_SUMMARY, Integer.class);
		givenClientSentCommandAndReceivedResponse(CMD_DEPOSIT_CASH, "{\"amount\": " + AMOUNT + "}");

		whenClientSendsCommand(CMD_DEPOSIT_CASH, "{\"amount\": " + AMOUNT + "}");

		thenShouldReceiveReply(AMOUNT + AMOUNT);
    }

	private void thenShouldReceiveReply(Object expectedResponse) throws Exception {

		Object actualResponse = receivedResponses.poll(SECONDS_TO_WAIT_FOR_RESPONSE, TimeUnit.SECONDS);
		assertThat("timeout waiting for event", actualResponse, notNullValue());
		assertThat(actualResponse, is(expectedResponse));
	}

	private void givenClientConnectedTo(String uri) throws Exception {
		
		session = stompClient
                .connect(uri, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
	}
    
	private Subscription givenClientSubscribesTo(String destination, Class<?> responseType) {
		
		return session.subscribe(destination, new StompConversation(responseType));
	}

	private Receiptable givenClientSentCommandAndReceivedResponse(String name, String payload) throws Exception {

		Receiptable receiptable = session.send(String.format("%s/%s", SEND_COMMAND_DESTINATION, name), payload);
		Object actualResponse = receivedResponses.poll(SECONDS_TO_WAIT_FOR_RESPONSE, TimeUnit.SECONDS);
		assertThat("timeout waiting for event", actualResponse, notNullValue());
		assertThat(receivedResponses.isEmpty(), is(true));
		return receiptable;
	}

	private Receiptable whenClientSendsCommand(String name, String payload) {

		return session.send(String.format("%s/%s", SEND_COMMAND_DESTINATION, name), payload);
	}

    class StompConversation implements StompFrameHandler {
    	
    	private Class<?> clazz;
    	
    	public StompConversation(Class<?> clazz) {
    		this.clazz = clazz;
    	}
    	
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return clazz;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
        	if (clazz.isAssignableFrom(o.getClass())) {
                receivedResponses.offer(o);
        	} else {
        		fail("Unexpected reponse received: " + "[" + o.getClass().getName() + "@" + Integer.toHexString(o.hashCode()) + "]" + o.toString());
        	}
        }
    }
}
