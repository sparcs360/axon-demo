package com.sparcs.counter.config;

import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rabbitmq.client.Channel;

@Configuration
public class KioskEventConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KioskEventConfig.class);

    @Autowired
    private CounterProperties counterProperties;

    @Bean
    @Profile("!DisableAmqp")
    public AmqpAdmin kioskEventAdmin(ConnectionFactory connectionFactory) {

        LOG.debug("kioskEventAdmin(connectionFactory={})", connectionFactory);

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(kioskEventExchange());
        admin.declareQueue(kioskEventQueue());
        admin.declareBinding(kioskEventBinding());
        return admin;
    }

    @Bean
    FanoutExchange kioskEventExchange() {

        LOG.debug("kioskEventExchange() <- getAmqpKioskEventsInExchangeName={}", counterProperties.getAmqpKioskEventsInExchangeName());

        FanoutExchange exchange = new FanoutExchange(counterProperties.getAmqpKioskEventsInExchangeName(), true, false);
        return exchange;
    }

    @Bean
    public Queue kioskEventQueue() {

        LOG.debug("kioskEventQueue() <- getAmqpKioskEventsInQueueName={}", counterProperties.getAmqpKioskEventsInQueueName());

        Queue queue = new Queue(counterProperties.getAmqpKioskEventsInQueueName(), false, false, true);
        return queue;
    }

    @Bean
    Binding kioskEventBinding() {

        LOG.debug("kioskEventBinding() <- queueName={}, exchangeName={}",
                counterProperties.getAmqpKioskEventsInExchangeName(), counterProperties.getAmqpKioskEventsInExchangeName());

        Binding binding = new Binding(counterProperties.getAmqpKioskEventsInQueueName(), Binding.DestinationType.QUEUE,
                counterProperties.getAmqpKioskEventsInExchangeName(), "", null);
        return binding;
    }

    @Bean
    public SpringAMQPMessageSource kioskEventMessageSource(Serializer serializer) {

        SpringAMQPMessageSource messageSource = new SpringAMQPMessageSource(serializer) {

            @RabbitListener(queues = "#{@counterProperties.amqpKioskEventsInQueueName}")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {

                super.onMessage(message, channel);
            }
        };
        return messageSource;
    }

    @Autowired
    public void registerKioskEventProcessors(EventHandlingConfiguration config, SpringAMQPMessageSource source) {

        config.registerSubscribingEventProcessor("com.sparcs.counter.kioskeventprocessors", c -> source);
    }
}
