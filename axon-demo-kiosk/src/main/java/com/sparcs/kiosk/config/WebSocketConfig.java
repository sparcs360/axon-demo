package com.sparcs.kiosk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.sparcs.instrumentation.LoggingHandshakeInterceptor;
import com.sparcs.spring.EnvironmentUtils;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	private final KioskProperties kioskProperties;
    private final Environment environment;

    @Autowired
    WebSocketConfig(KioskProperties kioskProperties, Environment environment) {

    	this.kioskProperties = kioskProperties;
    	this.environment = environment;
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	
        registry.addEndpoint("/kiosk-websocket")
        		.addInterceptors(new LoggingHandshakeInterceptor())
        		.withSockJS();
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	
        config.setApplicationDestinationPrefixes("/kiosk/commands/");
        if (EnvironmentUtils.isProfileActive(environment, "relay-ui-messages")) {
            config.enableStompBrokerRelay("/topic")
                  .setRelayHost(kioskProperties.getAmqpHostName());
        } else {
            config.enableSimpleBroker("/topic");
        }
    }
}
