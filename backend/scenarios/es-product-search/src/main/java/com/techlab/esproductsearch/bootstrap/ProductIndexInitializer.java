package com.techlab.esproductsearch.bootstrap;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.techlab.esproductsearch.service.ProductIndexService;

@Component
public class ProductIndexInitializer {

    private final ProductIndexService productIndexService;

    public ProductIndexInitializer(ProductIndexService productIndexService) {
        this.productIndexService = productIndexService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ensureIndexOnStartup() {
        productIndexService.ensureIndexExists();
    }
}
