package com.sparcs.counter.config;

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

	private final CounterProperties counterProperties;
    private final Environment environment;

    @Autowired
    WebSocketConfig(CounterProperties counterProperties, Environment environment) {
    	
    	this.counterProperties = counterProperties;
    	this.environment = environment;
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	
        registry.addEndpoint("/counter-websocket")
        		.addInterceptors(new LoggingHandshakeInterceptor())
        		.withSockJS();
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	
        config.setApplicationDestinationPrefixes("/counter/commands/");
        if (EnvironmentUtils.isProfileActive(environment, "relay-ui-messages")) {
            config.enableStompBrokerRelay("/topic")
                  .setRelayHost(counterProperties.getAmqpHostName());
        } else {
            config.enableSimpleBroker("/topic");
        }
    }
}
