package com.techlab.esproductsearch.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppElasticsearchProperties.class)
public class ElasticsearchConfig {
}
