package com.techlab.esproductsearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.elasticsearch")
public record AppElasticsearchProperties(String indexName) {
}
