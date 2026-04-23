package com.techlab.esproductsearch.service;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

import com.techlab.esproductsearch.config.AppElasticsearchProperties;

@Service
public class ProductIndexService {

    private static final String INDEX_MAPPING_JSON = """
            {
              "properties": {
                "name": { "type": "text" },
                "description": { "type": "text" },
                "category": { "type": "keyword" },
                "tags": { "type": "keyword" },
                "price": { "type": "double" },
                "inStock": { "type": "boolean" },
                "createdAt": { "type": "date" }
              }
            }
            """;

    private final ElasticsearchOperations operations;
    private final AppElasticsearchProperties appElasticsearchProperties;

    public ProductIndexService(
            ElasticsearchOperations operations,
            AppElasticsearchProperties appElasticsearchProperties
    ) {
        this.operations = operations;
        this.appElasticsearchProperties = appElasticsearchProperties;
    }

    public String indexName() {
        return appElasticsearchProperties.indexName();
    }

    public void ensureIndexExists() {
        IndexOperations indexOps = indexOps();
        if (!indexOps.exists()) {
            indexOps.create();
            indexOps.putMapping(Document.parse(INDEX_MAPPING_JSON));
        }
    }

    public void recreateIndex() {
        IndexOperations indexOps = indexOps();
        if (indexOps.exists()) {
            indexOps.delete();
        }
        indexOps.create();
        indexOps.putMapping(Document.parse(INDEX_MAPPING_JSON));
    }

    private IndexOperations indexOps() {
        return operations.indexOps(IndexCoordinates.of(appElasticsearchProperties.indexName()));
    }
}
