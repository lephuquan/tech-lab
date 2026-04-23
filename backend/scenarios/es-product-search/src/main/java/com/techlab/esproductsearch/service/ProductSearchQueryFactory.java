package com.techlab.esproductsearch.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

@Component
public class ProductSearchQueryFactory {

    public Query buildSearchQuery(ProductSearchCriteria criteria) {
        return Query.of(q -> q.bool(b -> {
            if (StringUtils.hasText(criteria.keyword())) {
                b.must(m -> m.multiMatch(mm -> mm
                        .query(criteria.keyword())
                        .fields("name^3", "description^2", "category", "tags")
                ));
            }

            if (StringUtils.hasText(criteria.category())) {
                b.filter(f -> f.term(t -> t
                        .field("category")
                        .value(criteria.category().trim().toLowerCase())
                ));
            }

            if (criteria.minPrice() != null || criteria.maxPrice() != null) {
                b.filter(f -> f.range(r -> {
                    r.field("price");
                    if (criteria.minPrice() != null) {
                        r.gte(JsonData.of(criteria.minPrice()));
                    }
                    if (criteria.maxPrice() != null) {
                        r.lte(JsonData.of(criteria.maxPrice()));
                    }
                    return r;
                }));
            }

            if (criteria.inStockOnly()) {
                b.filter(f -> f.term(t -> t.field("inStock").value(true)));
            }

            return b;
        }));
    }

    public Query buildPrefixSuggestionQuery(String prefix) {
        return Query.of(q -> q.matchPhrasePrefix(m -> m
                .field("name")
                .query(prefix)
        ));
    }
}
