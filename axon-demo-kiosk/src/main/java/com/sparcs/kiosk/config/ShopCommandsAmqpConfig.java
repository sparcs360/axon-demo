package com.sparcs.kiosk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ShopCommandsAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(ShopCommandsAmqpConfig.class);

	@Autowired
	private KioskProperties kioskProperties;
	
    @Bean
    @Profile("!DisableAmqp")
    public AmqpAdmin shopCommandAdmin(ConnectionFactory amqpConnectionFactory) {

        LOG.debug("shopCommandAdmin(amqpConnectionFactory={})", amqpConnectionFactory);
        
        RabbitAdmin admin = new RabbitAdmin(amqpConnectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(shopCommandExchange());
        admin.declareQueue(shopCommandQueue());
        admin.declareBinding(shopCommandBinding());
        return admin;
    }

    @Bean
    DirectExchange shopCommandExchange() {

        LOG.debug("shopCommandExchange() <- amqpShopExchangeName={}", kioskProperties.getAmqpShopExchangeName());
        DirectExchange exchange = new DirectExchange(kioskProperties.getAmqpShopExchangeName(), true, false);
		return exchange;
    }

    @Bean
    public Queue shopCommandQueue() {

        LOG.debug("shopCommandQueue() <- amqpShopQueueName={}", kioskProperties.getAmqpShopQueueName());
        
    	Queue queue = new Queue(kioskProperties.getAmqpShopQueueName(), false, false, true);
		return queue;
    }

    @Bean
    Binding shopCommandBinding() {
    	
        LOG.debug("shopCommandBinding() <- amqpShopQueueName={}, amqpShopExchangeName={}",
        		kioskProperties.getAmqpShopQueueName(), kioskProperties.getAmqpShopExchangeName());

        Binding binding = new Binding(kioskProperties.getAmqpShopQueueName(), Binding.DestinationType.QUEUE,
        		kioskProperties.getAmqpShopExchangeName(), kioskProperties.getKioskId(), null);
		return binding;
    }
}
