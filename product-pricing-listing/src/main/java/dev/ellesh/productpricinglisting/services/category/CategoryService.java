package dev.ellesh.productpricinglisting.services.category;

import dev.ellesh.productpricinglisting.exceptions.NoSuchEntityException;
import dev.ellesh.productpricinglisting.models.Category;

import java.util.List;


public interface CategoryService {
      Category createCategory(String title) ;
      Category updateCategory(Long id, String title) throws NoSuchEntityException;
      void deleteCategory(Long id);
      public List<Category> getAllCategories();
      public Category getCategoryById(Long id) throws NoSuchEntityException ;
}
