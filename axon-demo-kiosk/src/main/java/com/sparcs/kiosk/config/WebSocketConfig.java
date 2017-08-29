package com.sparcs.kiosk.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.sparcs.kiosk.instrumentation.LoggingHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private final Environment environment;

    @Autowired
    WebSocketConfig(Environment environment) {
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
        if (isProfileActive("relay-ui-messages")) {
            config.enableStompBrokerRelay("/topic")
                  .setRelayHost("localhost");
        } else {
            config.enableSimpleBroker("/topic");
        }
    }

	private boolean isProfileActive(String profileName) {
		return Arrays.stream(environment.getActiveProfiles()).anyMatch(activeProfileName -> activeProfileName.equals(profileName));
	}
}
