package com.techlab.esproductsearch.service;

import com.techlab.esproductsearch.domain.ProductDocument;

public record ProductSearchHitView(ProductDocument product, float score) {
}
