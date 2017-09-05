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
public class KioskCommandAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(KioskCommandAmqpConfig.class);

	@Autowired
	private CounterProperties counterProperties;
	
    @Bean
    public AmqpAdmin kioskCommandAdmin(ConnectionFactory amqpConnectionFactory) {

        LOG.debug("kioskCommandAdmin()");

    	RabbitAdmin admin = new RabbitAdmin(amqpConnectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(kioskCommandExchange());
        return admin;
    }

    @Bean
    DirectExchange kioskCommandExchange() {

        LOG.debug("kioskCommandExchange() <- getAmqpKioskCommandsOutExchangeName={}", counterProperties.getAmqpKioskCommandsOutExchangeName());
        
        DirectExchange exchange = new DirectExchange(counterProperties.getAmqpKioskCommandsOutExchangeName(), true, false);
		return exchange;
    }
}
