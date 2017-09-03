package com.sparcs.kiosk.config;

import org.axonframework.amqp.eventhandling.spring.SpringAMQPPublisher;
import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedEventBusAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedEventBusAmqpConfig.class);

	public static final String PROPERTY_PATH = "kiosk.eventbus-amqp";
	
	@Autowired
	@Value("${kiosk.id}")
	private String kioskId;
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".host-name}")
	private String hostName;
	
	@Autowired
	@Value("${" + PROPERTY_PATH + ".exchange-name}")
	private String exchangeName;
	
    @Bean
    public ConnectionFactory distributedEventBusConnectionFactory() {

        return new CachingConnectionFactory(hostName);
    }
    
    @Bean
    public AmqpAdmin distributedEventBusAdmin() {

        LOG.debug("distributedEventBusAdmin()");
        
        RabbitAdmin admin = new RabbitAdmin(distributedEventBusConnectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(distributedEventBusExchange());
        return admin;
    }
    
    @Bean
    FanoutExchange distributedEventBusExchange() {

        LOG.debug("distributedEventBusExchange() <- exchangeName={}", exchangeName);
        FanoutExchange exchange = new FanoutExchange(exchangeName, true, false);
		return exchange;
    }
    
	@Bean
    SpringAMQPPublisher amqpEventPublisher(EventBus eventBus, ConnectionFactory distributedEventBusConnectionFactory) {

		LOG.debug("amqpEventPublisher(eventBus={}, distributedEventBusConnectionFactory={}) <- exchangeName={}",
				eventBus, distributedEventBusConnectionFactory, exchangeName);

		SpringAMQPPublisher eventPublisher = new SpringAMQPPublisher(eventBus);
		eventPublisher.setConnectionFactory(distributedEventBusConnectionFactory);
		eventPublisher.setExchangeName(exchangeName);
		eventPublisher.start();
		return eventPublisher;
    }
}
