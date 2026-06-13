package dev.ellesh.productsearch.repositories;


import dev.ellesh.productsearch.models.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<Product, String> {
}
