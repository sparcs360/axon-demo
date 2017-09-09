package com.sparcs.kiosk.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("discovery")
@EnableDiscoveryClient
public class DiscoveryConfig {

}
