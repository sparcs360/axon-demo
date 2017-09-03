package com.sparcs.counter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopCommandsAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(ShopCommandsAmqpConfig.class);

	@Autowired
	private CounterProperties counterProperties;
	
    @Bean
    public AmqpAdmin shopCommandAdmin(ConnectionFactory amqpConnectionFactory) {

        LOG.debug("shopCommandAdmin()");

    	RabbitAdmin admin = new RabbitAdmin(amqpConnectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(shopCommandExchange());
        return admin;
    }

    @Bean
    DirectExchange shopCommandExchange() {

        LOG.debug("shopCommandExchange() <- exchangeName={}", counterProperties.getAmqpShopExchangeName());
        
        DirectExchange exchange = new DirectExchange(counterProperties.getAmqpShopExchangeName(), true, false);
		return exchange;
    }
}
