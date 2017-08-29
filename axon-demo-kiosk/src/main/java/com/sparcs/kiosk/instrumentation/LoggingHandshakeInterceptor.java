package com.sparcs.kiosk.instrumentation;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class LoggingHandshakeInterceptor implements HandshakeInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingHandshakeInterceptor.class);
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    	LOG.trace("beforeHandshake(request={}, response={}, wsHandler={}, attributes={})", request, response, wsHandler, attributes);
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

    	LOG.trace("afterHandshake(request={}, response={}, wsHandler={}, exception={})", request, response, wsHandler, exception);
	}
}
