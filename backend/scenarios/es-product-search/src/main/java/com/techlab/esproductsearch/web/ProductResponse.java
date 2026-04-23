package com.techlab.esproductsearch.web;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.techlab.esproductsearch.domain.ProductDocument;

public record ProductResponse(
        String id,
        String name,
        String description,
        String category,
        List<String> tags,
        BigDecimal price,
        boolean inStock,
        Instant createdAt,
        Float score
) {

    public static ProductResponse from(ProductDocument document) {
        return new ProductResponse(
                document.getId(),
                document.getName(),
                document.getDescription(),
                document.getCategory(),
                document.getTags(),
                document.getPrice(),
                document.isInStock(),
                document.getCreatedAt(),
                null
        );
    }

    public static ProductResponse from(ProductDocument document, float score) {
        return new ProductResponse(
                document.getId(),
                document.getName(),
                document.getDescription(),
                document.getCategory(),
                document.getTags(),
                document.getPrice(),
                document.isInStock(),
                document.getCreatedAt(),
                score
        );
    }
}
