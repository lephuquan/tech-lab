package com.techlab.esproductsearch.service;

import java.util.List;

public record ProductSearchResult(
        List<ProductSearchHitView> items,
        long total,
        int page,
        int size
) {
}
