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
public class DistributedCommandAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedCommandAmqpConfig.class);

	@Autowired
	private KioskProperties kioskProperties;
	
    @Bean
    @Profile("!DisableAmqp")
    public AmqpAdmin distributedCommandAdmin(ConnectionFactory connectionFactory) {

        LOG.debug("distributedCommandAdmin(connectionFactory={})", connectionFactory);
        
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(distributedCommandExchange());
        admin.declareQueue(distributedCommandQueue());
        admin.declareBinding(distributedCommandBinding());
        return admin;
    }

    @Bean
    DirectExchange distributedCommandExchange() {

        LOG.debug("distributedCommandExchange() <- getAmqpKioskCommandsInExchangeName={}", kioskProperties.getAmqpKioskCommandsInExchangeName());
        DirectExchange exchange = new DirectExchange(kioskProperties.getAmqpKioskCommandsInExchangeName(), true, false);
		return exchange;
    }

    @Bean
    public Queue distributedCommandQueue() {

        LOG.debug("distributedCommandQueue() <- getAmqpKioskCommandsInQueueName={}", kioskProperties.getAmqpKioskCommandsInQueueName());
        
    	Queue queue = new Queue(kioskProperties.getAmqpKioskCommandsInQueueName(), false, false, true);
		return queue;
    }

    @Bean
    Binding distributedCommandBinding() {
    	
        LOG.debug("distributedCommandBinding() <- getAmqpKioskCommandsInExchangeName={}, getAmqpKioskCommandsInQueueName={}",
        		kioskProperties.getAmqpKioskCommandsInExchangeName(), kioskProperties.getAmqpKioskCommandsInQueueName());

        Binding binding = new Binding(kioskProperties.getAmqpKioskCommandsInQueueName(), Binding.DestinationType.QUEUE,
        		kioskProperties.getAmqpKioskCommandsInExchangeName(), kioskProperties.getKioskId(), null);
		return binding;
    }
}
