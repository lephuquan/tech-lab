package com.techlab.esproductsearch.web;

import java.util.List;

public record ProductSearchResponse(
        List<ProductResponse> items,
        long total,
        int page,
        int size
) {
}
