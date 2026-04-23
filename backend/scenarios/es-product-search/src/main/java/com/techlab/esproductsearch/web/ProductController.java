package com.techlab.esproductsearch.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.esproductsearch.service.ProductCatalogService;
import com.techlab.esproductsearch.service.ProductSearchCriteria;
import com.techlab.esproductsearch.service.ProductSearchResult;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductCatalogService productCatalogService;

    public ProductController(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> indexOne(@Valid @RequestBody IndexProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(productCatalogService.indexOne(request)));
    }

    @PostMapping("/seed")
    public ResponseEntity<SeedResponse> seedData() {
        int count = productCatalogService.seedDefaultCatalog();
        return ResponseEntity.ok(new SeedResponse("Seed du lieu mau thanh cong", count));
    }

    @PostMapping("/reindex-sample")
    public ResponseEntity<SeedResponse> recreateAndSeed() {
        int count = productCatalogService.recreateAndSeedDefaultCatalog();
        return ResponseEntity.ok(new SeedResponse("Recreate index va nap du lieu mau thanh cong", count));
    }

    @GetMapping("/search")
    public ResponseEntity<ProductSearchResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "false") boolean inStockOnly,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                keyword,
                category,
                minPrice,
                maxPrice,
                inStockOnly,
                page,
                size
        );
        ProductSearchResult result = productCatalogService.search(criteria);
        List<ProductResponse> items = result.items().stream()
                .map(hit -> ProductResponse.from(hit.product(), hit.score()))
                .toList();

        return ResponseEntity.ok(new ProductSearchResponse(items, result.total(), result.page(), result.size()));
    }

    @GetMapping("/suggest")
    public ResponseEntity<SuggestResponse> suggest(
            @RequestParam @NotBlank String prefix,
            @RequestParam(defaultValue = "5") @Min(1) int limit
    ) {
        return ResponseEntity.ok(new SuggestResponse(productCatalogService.suggestByPrefix(prefix, limit)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(ProductResponse.from(productCatalogService.getById(id)));
    }
}
