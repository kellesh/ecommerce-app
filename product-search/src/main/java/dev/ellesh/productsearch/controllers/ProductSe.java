package dev.ellesh.productsearch.controllers;

import dev.ellesh.productsearch.models.Product;
import dev.ellesh.productsearch.models.Product;
import dev.ellesh.productsearch.services.ProductSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/search/products")
public class ProductSearchController {

    private final ProductSearchService service;

    public ProductSearchController(ProductSearchService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> createOrUpdate(@RequestBody Product product) {
        return ResponseEntity.ok(service.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/advanced")
    public ResponseEntity<Page<Product>> advancedSearch(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "relevance") String sortBy, // relevance, priceAsc, priceDesc
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> results = service.advancedSearch(
                q, categoryIds, minPrice, maxPrice, sortBy, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(results);
    }


}
