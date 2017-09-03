package com.sparcs.counter.config;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AxonConfig.class);
	
    @Bean
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
}
