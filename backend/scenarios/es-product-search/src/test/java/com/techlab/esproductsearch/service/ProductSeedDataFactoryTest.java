package com.techlab.esproductsearch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class ProductSeedDataFactoryTest {

    private final ProductSeedDataFactory productSeedDataFactory = new ProductSeedDataFactory();

    @Test
    void shouldBuildDefaultCatalogWithExpectedSizeAndUniqueIds() {
        var products = productSeedDataFactory.buildDefaultCatalog();

        assertEquals(8, products.size());
        Set<String> ids = products.stream()
                .map(product -> product.getId())
                .collect(Collectors.toSet());
        assertEquals(products.size(), ids.size());
        assertTrue(products.stream().allMatch(product -> product.getPrice().signum() > 0));
    }
}
