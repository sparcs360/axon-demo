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
public class KioskEventConfig {

	private static final Logger LOG = LoggerFactory.getLogger(KioskEventConfig.class);

	@Autowired
	private KioskProperties kioskProperties;
	
    @Bean
    @Profile("!DisableAmqp")
    public AmqpAdmin kioskEventAdmin(ConnectionFactory connectionFactory) {

        LOG.debug("kioskEventAdmin(connectionFactory={})", connectionFactory);
        
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(kioskEventExchange());
        return admin;
    }
    
    @Bean
    FanoutExchange kioskEventExchange() {

        LOG.debug("kioskEventExchange() <- getAmqpKioskEventsOutExchangeName={}", kioskProperties.getAmqpKioskEventsOutExchangeName());

        FanoutExchange exchange = new FanoutExchange(kioskProperties.getAmqpKioskEventsOutExchangeName(), true, false);
		return exchange;
    }
    
	@Bean
    @Profile("!DisableAmqp")
    SpringAMQPPublisher kioskEventPublisher(EventBus eventBus, ConnectionFactory connectionFactory) {

		LOG.debug("kioskEventPublisher(eventBus={}, connectionFactory={}) <- getAmqpKioskEventsOutExchangeName={}",
				eventBus, connectionFactory, kioskProperties.getAmqpKioskEventsOutExchangeName());

		SpringAMQPPublisher eventPublisher = new SpringAMQPPublisher(eventBus);
		eventPublisher.setConnectionFactory(connectionFactory);
		eventPublisher.setExchangeName(kioskProperties.getAmqpKioskEventsOutExchangeName());
		eventPublisher.start();
		return eventPublisher;
    }
}
