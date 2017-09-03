package com.sparcs.counter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class ShopCommandsAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(ShopCommandsAmqpConfig.class);

	public static final String PROPERTY_PATH = "counter.shop-amqp";
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".host-name}")
	private String hostName;
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".exchange-name}")
	private String exchangeName;
	
    @Bean
    public ConnectionFactory connectionFactory() {

        LOG.debug("connectionFactory() <- hostName={}", hostName);
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(hostName);
		return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin shopCommandAdmin() {

        LOG.debug("shopCommandAdmin()");

    	RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(shopCommandExchange());
        return admin;
    }

    @Bean
    DirectExchange shopCommandExchange() {

        LOG.debug("shopCommandExchange() <- exchangeName={}", exchangeName);
        DirectExchange exchange = new DirectExchange(exchangeName, true, false);
		return exchange;
    }
    
    @Bean
    public AmqpTemplate shopCommandAmqpTemplate(Jackson2JsonMessageConverter messageConverter) {

    	RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory());
        amqpTemplate.setConnectionFactory(connectionFactory());
        amqpTemplate.setExchange(exchangeName);
        amqpTemplate.setMessageConverter(messageConverter);
		return amqpTemplate;
    }

    @Bean
	Jackson2JsonMessageConverter shopCommandMessageConverter(ObjectMapper shopCommandObjectMapper) {
    	
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
