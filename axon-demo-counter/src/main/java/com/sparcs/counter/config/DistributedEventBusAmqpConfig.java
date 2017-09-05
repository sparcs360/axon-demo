package com.sparcs.counter.config;

import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventHandlerInvoker;
import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.SubscribingEventProcessor;
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
import com.sparcs.kiosk.executive.account.EBalanceChanged;

@Configuration
public class DistributedEventBusAmqpConfig {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedEventBusAmqpConfig.class);

	@Autowired
	private CounterProperties counterProperties;

	@Bean
	@Profile("!DisableAmqp")
	public AmqpAdmin distributedEventBusAdmin(ConnectionFactory amqpConnectionFactory) {

        LOG.debug("distributedEventBusAdmin(amqpConnectionFactory={})", amqpConnectionFactory);

		RabbitAdmin admin = new RabbitAdmin(amqpConnectionFactory);
		admin.setAutoStartup(true);
		admin.declareExchange(distributedEventBusExchange());
		admin.declareQueue(distributedEventBusQueue());
		admin.declareBinding(distributedEventBusBinding());
		return admin;
	}

	@Bean
	FanoutExchange distributedEventBusExchange() {

        LOG.debug("distributedEventBusExchange() <- getAmqpEventbusExchangeName={}", counterProperties.getAmqpEventbusExchangeName());

		FanoutExchange exchange = new FanoutExchange(counterProperties.getAmqpEventbusExchangeName(), true, false);
		return exchange;
	}

	@Bean
	public Queue distributedEventBusQueue() {

		LOG.debug("distributedEventBusQueue() <- queueName={}", counterProperties.getAmqpEventbusQueueName());

		Queue queue = new Queue(counterProperties.getAmqpEventbusQueueName(), false, false, true);
		return queue;
	}

	@Bean
	Binding distributedEventBusBinding() {

		LOG.debug("distributedEventBusBinding() <- queueName={}, exchangeName={}",
				counterProperties.getAmqpEventbusQueueName(), counterProperties.getAmqpEventbusExchangeName());

		Binding binding = new Binding(counterProperties.getAmqpEventbusQueueName(), Binding.DestinationType.QUEUE,
				counterProperties.getAmqpEventbusExchangeName(), "", null);
		return binding;
	}
    
	@Bean
	public SpringAMQPMessageSource distributedEventBusMessageSource(/* EventBus eventBus, */Serializer serializer) {

		SpringAMQPMessageSource messageSource = new SpringAMQPMessageSource(serializer) {

			@RabbitListener(queues="#{@counterProperties.amqpEventbusQueueName}")
			@Override
			public void onMessage(Message message, Channel channel) throws Exception {

				super.onMessage(message, channel);
			}
		};
		messageSource.subscribe(events -> events.forEach(event -> LOG.trace(event.getPayload().toString())));
		//messageSource.subscribe(events -> eventBus.publish(events));
		return messageSource;
	}

    @Bean
    public EventProcessor eventProcessor(EventBus eventBus) {
        EventProcessor eventProcessor = new SubscribingEventProcessor("eventProcessor", (EventHandlerInvoker) this, eventBus);
        eventProcessor.start();
        return eventProcessor;
    }
    
	@EventHandler
	void on(EBalanceChanged event) {
		
		LOG.info("{}", event);
	}
}
