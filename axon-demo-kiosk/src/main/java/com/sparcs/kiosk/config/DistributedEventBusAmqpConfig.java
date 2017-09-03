package com.sparcs.kiosk.config;

import org.axonframework.amqp.eventhandling.spring.SpringAMQPPublisher;
import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DistributedEventBusAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedEventBusAmqpConfig.class);

	@Autowired
	private KioskProperties kioskProperties;
	
    @Bean
    @Profile("!DisableAmqp")
    public AmqpAdmin distributedEventBusAdmin(ConnectionFactory amqpConnectionFactory) {

        LOG.debug("distributedEventBusAdmin(amqpConnectionFactory={})", amqpConnectionFactory);
        
        RabbitAdmin admin = new RabbitAdmin(amqpConnectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(distributedEventBusExchange());
        return admin;
    }
    
    @Bean
    FanoutExchange distributedEventBusExchange() {

        LOG.debug("distributedEventBusExchange() <- getAmqpEventbusExchangeName={}", kioskProperties.getAmqpEventbusExchangeName());

        FanoutExchange exchange = new FanoutExchange(kioskProperties.getAmqpEventbusExchangeName(), true, false);
		return exchange;
    }
    
	@Bean
    @Profile("!DisableAmqp")
    SpringAMQPPublisher amqpEventPublisher(EventBus eventBus, ConnectionFactory distributedEventBusConnectionFactory) {

		LOG.debug("amqpEventPublisher(eventBus={}, distributedEventBusConnectionFactory={}) <- getAmqpEventbusExchangeName={}",
				eventBus, distributedEventBusConnectionFactory, kioskProperties.getAmqpEventbusExchangeName());

		SpringAMQPPublisher eventPublisher = new SpringAMQPPublisher(eventBus);
		eventPublisher.setConnectionFactory(distributedEventBusConnectionFactory);
		eventPublisher.setExchangeName(kioskProperties.getAmqpEventbusExchangeName());
		eventPublisher.start();
		return eventPublisher;
    }
}
