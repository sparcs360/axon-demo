package com.sparcs.kiosk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class AmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AmqpConfig.class);

	@Autowired
	private KioskProperties kioskProperties;
	
    @Bean
    public ConnectionFactory amqpConnectionFactory() {

        LOG.debug("amqpConnectionFactory() <- amqpHostName={}", kioskProperties.getAmqpHostName());

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(kioskProperties.getAmqpHostName());
		return connectionFactory;
    }
    
    @Bean
	MessageConverter shopCommandMessageConverter(ObjectMapper shopCommandObjectMapper) {
    	
		Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(shopCommandObjectMapper);
		return messageConverter;
	}
    
    @Bean
    ObjectMapper shopCommandObjectMapper() {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    	return objectMapper;
    }
}
