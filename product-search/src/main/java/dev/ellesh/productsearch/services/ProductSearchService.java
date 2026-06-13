package dev.ellesh.productsearch.services;

import dev.ellesh.productsearch.models.Product;
import dev.ellesh.productsearch.repositories.ProductSearchRepository;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import co.elastic.clients.elasticsearch._types.FieldValue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSearchService {

    private final ProductSearchRepository productRepo;
    private final ElasticsearchOperations operations;

    public ProductSearchService(ProductSearchRepository productRepo, ElasticsearchOperations operations) {
        this.productRepo = productRepo;
        this.operations = operations;
    }

    // Create / Update
    public Product save(Product product) {
        return productRepo.save(product);
    }

    // Delete
    public void delete(String id) {
        productRepo.deleteById(id);
    }

    public Page<Product> advancedSearch(String text,
                                        List<Long> categoryIds,
                                        Double minPrice,
                                        Double maxPrice,
                                        String sortBy,
                                        Pageable pageable) {

        List<co.elastic.clients.elasticsearch._types.query_dsl.Query> mustQueries = new ArrayList<>();
        List<co.elastic.clients.elasticsearch._types.query_dsl.Query> filterQueries = new ArrayList<>();

        // Full-text search
        if (text != null && !text.isBlank()) {
            mustQueries.add(co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q -> q
                    .multiMatch(m -> m
                            .query(text)
                            .fields("name", "description", "categoryName")
                    )));
        }

        // Category filter
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<FieldValue> categoryValues = categoryIds.stream()
                    .map(FieldValue::of)
                    .toList();

            filterQueries.add(co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q -> q
                    .terms(t -> t
                            .field("categoryId")
                            .terms(tf -> tf.value(categoryValues))
                    )));
        }

        // Price range filter
        if (minPrice != null || maxPrice != null) {
            filterQueries.add(co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q -> q
                    .range(r -> {
                        r.field("price");
                        if (minPrice != null) {
                            r.gte(co.elastic.clients.json.JsonData.of(minPrice));
                        }
                        if (maxPrice != null) {
                            r.lte(co.elastic.clients.json.JsonData.of(maxPrice));
                        }
                        return r;
                    })
            ));
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            if (!mustQueries.isEmpty()) {
                                b.must(mustQueries);
                            }
                            if (!filterQueries.isEmpty()) {
                                b.filter(filterQueries);
                            }
                            return b;
                        })
                )
                .withPageable(pageable)
                .build();

        // Sorting
        if ("priceAsc".equalsIgnoreCase(sortBy)) {
            query.addSort(org.springframework.data.domain.Sort.by("price").ascending());
        } else if ("priceDesc".equalsIgnoreCase(sortBy)) {
            query.addSort(org.springframework.data.domain.Sort.by("price").descending());
        } else if ("relevance".equalsIgnoreCase(sortBy)) {
            query.addSort(org.springframework.data.domain.Sort.by("_score").descending());
        }

        //return operations.search(query, Product.class).map(hit -> hit.getContent());
        var searchHits = operations.search(query, Product.class);

        List<Product> products = searchHits.stream()
                .map(hit -> hit.getContent())
                .toList();

        return new org.springframework.data.domain.PageImpl<>(
                products,
                pageable,
                searchHits.getTotalHits()
        );
    }
}