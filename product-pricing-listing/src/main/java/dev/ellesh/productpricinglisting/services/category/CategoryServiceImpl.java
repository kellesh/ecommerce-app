package dev.ellesh.productpricinglisting.services.category;

import dev.ellesh.productpricinglisting.exceptions.NoSuchEntityException;
import dev.ellesh.productpricinglisting.models.Category;
import dev.ellesh.productpricinglisting.repositories.CategoryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String title) {
        Category category = new Category();
        category.setTitle(title);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, String title) throws NoSuchEntityException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Category not found with id: " + id));
        category.setTitle(title);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) throws NoSuchEntityException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Category not found with id: " + id));
    }
}
