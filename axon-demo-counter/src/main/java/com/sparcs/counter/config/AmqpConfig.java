package com.sparcs.counter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AmqpConfig.class);

	@Autowired
	private CounterProperties counterProperties;
	
    @Bean
    public ConnectionFactory amqpConnectionFactory() {

        LOG.debug("amqpConnectionFactory() <- amqpHostName={}", counterProperties.getAmqpHostName());

        return new CachingConnectionFactory(counterProperties.getAmqpHostName());
    }
}
