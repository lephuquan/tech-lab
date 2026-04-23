package com.techlab.esproductsearch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProductSearchCriteriaTest {

    @Test
    void shouldNormalizeInvalidPageAndOversizedLimit() {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                "iphone",
                null,
                null,
                null,
                false,
                -7,
                1000
        );

        ProductSearchCriteria normalized = criteria.normalized();

        assertEquals(0, normalized.page());
        assertEquals(100, normalized.size());
    }

    @Test
    void shouldNormalizeSizeToMinimumOne() {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                null,
                null,
                null,
                null,
                false,
                0,
                0
        );

        ProductSearchCriteria normalized = criteria.normalized();

        assertEquals(1, normalized.size());
    }
}
