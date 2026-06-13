package dev.ellesh.productpricinglisting.services.product;

import dev.ellesh.productpricinglisting.exceptions.NoSuchEntityException;
import dev.ellesh.productpricinglisting.models.Category;
import dev.ellesh.productpricinglisting.models.Product;
import dev.ellesh.productpricinglisting.repositories.CategoryRepository;
import dev.ellesh.productpricinglisting.repositories.ProductRepository;

import dev.ellesh.productpricinglisting.services.product.ProductService;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(String name, String description, Long categoryId,
                                 BigDecimal maximumRetailPrice, String imageUrl) throws NoSuchEntityException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchEntityException("Category not found with id: " + categoryId));

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setMaximumRetailPrice(maximumRetailPrice);
        product.setImageUrl(imageUrl);

        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) throws NoSuchEntityException {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(Long id) throws NoSuchEntityException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long id) throws NoSuchEntityException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Product not found with id: " + id));
        // You can add update logic here (e.g., set new fields from DTO)
        return productRepository.save(product);
    }
}
