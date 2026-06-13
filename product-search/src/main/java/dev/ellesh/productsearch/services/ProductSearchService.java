package dev.ellesh.productsearch.services;

import dev.ellesh.productsearch.models.Product;
import dev.ellesh.productsearch.repositories.ProductSearchRepository;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

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

    public Page<ProductDocument> advancedSearch(String text,
                                                List<Long> categoryIds,
                                                Double minPrice,
                                                Double maxPrice,
                                                String sortBy,   // "priceAsc", "priceDesc", "relevance"
                                                Pageable pageable) {
        var boolQuery = QueryBuilders.boolQuery();

        // Full-text search
        if (text != null && !text.isBlank()) {
            boolQuery.must(QueryBuilders.multiMatchQuery(text, "name", "description", "categoryName"));
        }

        // Category filter
        if (categoryIds != null && !categoryIds.isEmpty()) {
            boolQuery.filter(QueryBuilders.termsQuery("categoryId", categoryIds));
        }

        // Price range filter
        if (minPrice != null || maxPrice != null) {
            var range = QueryBuilders.rangeQuery("price");
            if (minPrice != null) range.gte(minPrice);
            if (maxPrice != null) range.lte(maxPrice);
            boolQuery.filter(range);
        }

        var queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable);

        // Sorting
        if ("priceAsc".equalsIgnoreCase(sortBy)) {
            queryBuilder.withSort(org.springframework.data.domain.Sort.by("price").ascending());
        } else if ("priceDesc".equalsIgnoreCase(sortBy)) {
            queryBuilder.withSort(org.springframework.data.domain.Sort.by("price").descending());
        } else if ("relevance".equalsIgnoreCase(sortBy)) {
            queryBuilder.withSort(org.springframework.data.domain.Sort.by("_score").descending());
        }

        var query = queryBuilder.build();
        return operations.search(query, ProductDocument.class).map(hit -> hit.getContent());
    }

}
