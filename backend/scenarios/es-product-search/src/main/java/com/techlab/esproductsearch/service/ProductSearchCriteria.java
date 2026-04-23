package com.techlab.esproductsearch.service;

import java.math.BigDecimal;

public record ProductSearchCriteria(
        String keyword,
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        boolean inStockOnly,
        int page,
        int size
) {

    public ProductSearchCriteria normalized() {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = Math.max(1, Math.min(size, 100));
        return new ProductSearchCriteria(
                keyword,
                category,
                minPrice,
                maxPrice,
                inStockOnly,
                normalizedPage,
                normalizedSize
        );
    }
}
