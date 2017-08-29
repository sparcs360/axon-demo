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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sparcs.kiosk.instrumentation.LoggingCommandMonitor;
import com.sparcs.kiosk.instrumentation.LoggingEventMonitor;

@Configuration
public class AxonConfig {

    @Bean
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
    
	@Bean
    EventBus eventBus(EventStorageEngine eventStorageEngine, LoggingEventMonitor loggingEventMonitor) {
    	
		EventBus eventBus = new EmbeddedEventStore(eventStorageEngine, loggingEventMonitor);
        return eventBus;
    }

    @Bean
    @Qualifier("localSegment")
    public SimpleCommandBus commandBus(AxonConfiguration axonConfiguration, TransactionManager txManager, LoggingCommandMonitor loggingCommandMonitor) {

    	SimpleCommandBus commandBus = new SimpleCommandBus(txManager, loggingCommandMonitor);
        commandBus.registerHandlerInterceptor(new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders()));
        return commandBus;
    }
}
