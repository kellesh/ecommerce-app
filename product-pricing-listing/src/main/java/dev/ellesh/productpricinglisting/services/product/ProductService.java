package dev.ellesh.productpricinglisting.services.product;

import dev.ellesh.productpricinglisting.exceptions.NoSuchEntityException;
import dev.ellesh.productpricinglisting.models.Product;

import java.math.BigDecimal;

public interface ProductService {
    Product createProduct(String name, String description, Long categoryId, BigDecimal maximumRetailPrice, String imageUrl) throws  NoSuchEntityException;
    Product getProduct(Long id ) throws  NoSuchEntityException;
    void deleteProduct( Long id) throws NoSuchEntityException;
    Product updateProduct(Long id )  ;
}
