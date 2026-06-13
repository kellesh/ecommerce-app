package dev.ellesh.productpricinglisting.repositories;

import dev.ellesh.productpricinglisting.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long>, ListCrudRepository<Product, Long> {
}
