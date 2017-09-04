package com.sparcs.kiosk.config;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sparcs.instrumentation.LoggingCommandMonitor;
import com.sparcs.instrumentation.LoggingEventMonitor;

@Configuration
public class AxonConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AxonConfig.class);
	
    @Bean
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    @Qualifier("localSegment")
    public SimpleCommandBus commandBus(AxonConfiguration axonConfiguration, TransactionManager txManager, LoggingCommandMonitor loggingCommandMonitor) {

    	LOG.debug("commandBus(axonConfiguration={}, txManager={}, loggingCommandMonitor={})", axonConfiguration, txManager, loggingCommandMonitor);
    	
    	SimpleCommandBus commandBus = new SimpleCommandBus(txManager, loggingCommandMonitor);
        commandBus.registerHandlerInterceptor(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders()));
        return commandBus;
    }

	@Bean
    EventBus eventBus(EventStorageEngine eventStorageEngine, LoggingEventMonitor loggingEventMonitor) {
    	
		LOG.debug("eventBus(eventStorageEngine={}, loggingEventMonitor={})", eventStorageEngine, loggingEventMonitor);
		
		EventBus eventBus = new EmbeddedEventStore(eventStorageEngine, loggingEventMonitor);
        return eventBus;
    }
}
