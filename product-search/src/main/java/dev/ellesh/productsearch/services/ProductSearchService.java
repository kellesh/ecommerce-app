package dev.ellesh.productsearch.services;

import dev.ellesh.productsearch.models.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductSearchService {

    // Create / Update
    Product save(Product product);

    // Delete
     void delete(String id) ;

     Page<Product> advancedSearch(String text,
                                        List<Long> categoryIds,
                                        Double minPrice,
                                        Double maxPrice,
                                        String sortBy,
                                        Pageable pageable);
}