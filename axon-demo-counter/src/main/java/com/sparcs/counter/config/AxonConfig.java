package com.sparcs.counter.config;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
	
    @Bean
    Serializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
}
