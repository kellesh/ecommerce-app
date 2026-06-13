package dev.ellesh.productpricinglisting.repositories;

import dev.ellesh.productpricinglisting.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> , ListCrudRepository<Category, Long> {
}
