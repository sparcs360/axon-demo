package com.sparcs.kiosk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
public class ShopCommandsConfig {

	private static final Logger LOG = LoggerFactory.getLogger(ShopCommandsConfig.class);

	public static final String PROPERTY_PATH = "kiosk.shop-amqp";
	
	@Autowired
	@Value("${kiosk.id}")
	private String kioskId;
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".host-name}")
	private String hostName;
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".exchange-name}")
	private String exchangeName;
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".queue-name}")
	private String queueName;
	
    @Bean
    public ConnectionFactory connectionFactory() {

        return new CachingConnectionFactory(hostName);
    }

    @Bean
    public AmqpAdmin shopCommandAdmin() {

        LOG.info("shopCommandAdmin()");
        
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(shopCommandExchange());
        admin.declareQueue(shopCommandQueue());
        admin.declareBinding(shopCommandBinding());
        return admin;
    }

    @Bean
    DirectExchange shopCommandExchange() {

        LOG.info("shopCommandExchange() <- exchangeName={}", exchangeName);
        DirectExchange exchange = new DirectExchange(exchangeName, true, false);
		return exchange;
    }

    @Bean
    public Queue shopCommandQueue() {

        LOG.info("shopCommandQueue() <- queueName={}", queueName);
        
    	Queue queue = new Queue(queueName, false, false, true);
		return queue;
    }

    @Bean
    Binding shopCommandBinding() {
    	
        LOG.info("shopCommandBinding() <- queueName={}, exchangeName={}", queueName, exchangeName);

        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, kioskId, null);
		return binding;
    }

    @Bean
    public AmqpTemplate shopCommandAmqpTemplate(Jackson2JsonMessageConverter messageConverter) {

        RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory());
        amqpTemplate.setConnectionFactory(connectionFactory());
        amqpTemplate.setExchange(exchangeName);
        amqpTemplate.setQueue(queueName);
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
