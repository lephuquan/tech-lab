package com.techlab.esproductsearch.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.techlab.esproductsearch.domain.ProductDocument;
import com.techlab.esproductsearch.web.IndexProductRequest;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

@Service
public class ProductCatalogService {

    private final ElasticsearchOperations operations;
    private final ProductSearchQueryFactory queryFactory;
    private final ProductSeedDataFactory seedDataFactory;
    private final ProductIndexService productIndexService;

    public ProductCatalogService(
            ElasticsearchOperations operations,
            ProductSearchQueryFactory queryFactory,
            ProductSeedDataFactory seedDataFactory,
            ProductIndexService productIndexService
    ) {
        this.operations = operations;
        this.queryFactory = queryFactory;
        this.seedDataFactory = seedDataFactory;
        this.productIndexService = productIndexService;
    }

    public ProductDocument indexOne(IndexProductRequest request) {
        productIndexService.ensureIndexExists();

        String id = StringUtils.hasText(request.id()) ? request.id().trim() : UUID.randomUUID().toString();
        ProductDocument document = new ProductDocument(
                id,
                request.name().trim(),
                request.description().trim(),
                request.category().trim().toLowerCase(),
                request.tags(),
                request.price(),
                request.inStock(),
                Instant.now()
        );

        return operations.save(document, indexCoordinates());
    }

    public int seedDefaultCatalog() {
        productIndexService.ensureIndexExists();
        List<ProductDocument> docs = seedDataFactory.buildDefaultCatalog();
        docs.forEach(doc -> operations.save(doc, indexCoordinates()));
        return docs.size();
    }

    public int recreateAndSeedDefaultCatalog() {
        productIndexService.recreateIndex();
        return seedDefaultCatalog();
    }

    public ProductSearchResult search(ProductSearchCriteria rawCriteria) {
        ProductSearchCriteria criteria = rawCriteria.normalized();
        if (criteria.minPrice() != null && criteria.maxPrice() != null
                && criteria.minPrice().compareTo(criteria.maxPrice()) > 0) {
            throw new IllegalArgumentException("minPrice khong duoc lon hon maxPrice");
        }

        Query query = queryFactory.buildSearchQuery(criteria);
        NativeQueryBuilder queryBuilder = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(criteria.page(), criteria.size()));

        if (!StringUtils.hasText(criteria.keyword())) {
            queryBuilder.withSort(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        SearchHits<ProductDocument> hits = operations.search(queryBuilder.build(), ProductDocument.class, indexCoordinates());
        List<ProductSearchHitView> items = hits.stream()
                .map(this::toHitView)
                .toList();

        return new ProductSearchResult(items, hits.getTotalHits(), criteria.page(), criteria.size());
    }

    public List<String> suggestByPrefix(String prefix, int limit) {
        int normalizedLimit = Math.max(1, Math.min(limit, 20));
        Query query = queryFactory.buildPrefixSuggestionQuery(prefix);
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(0, normalizedLimit))
                .build();

        SearchHits<ProductDocument> hits = operations.search(nativeQuery, ProductDocument.class, indexCoordinates());
        List<String> suggestions = new ArrayList<>();
        for (SearchHit<ProductDocument> hit : hits.getSearchHits()) {
            if (!suggestions.contains(hit.getContent().getName())) {
                suggestions.add(hit.getContent().getName());
            }
        }
        return suggestions;
    }

    public ProductDocument getById(String id) {
        ProductDocument found = operations.get(id, ProductDocument.class, indexCoordinates());
        if (found == null) {
            throw new NoSuchElementException("Khong tim thay product voi id: " + id);
        }
        return found;
    }

    private ProductSearchHitView toHitView(SearchHit<ProductDocument> searchHit) {
        return new ProductSearchHitView(searchHit.getContent(), searchHit.getScore());
    }

    private IndexCoordinates indexCoordinates() {
        return IndexCoordinates.of(productIndexService.indexName());
    }
}
