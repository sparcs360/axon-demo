package com.sparcs.product.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("discovery")
@EnableDiscoveryClient
public class DiscoveryConfig {

}
