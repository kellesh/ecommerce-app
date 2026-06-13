package dev.ellesh.productpricinglisting.controllers.product;

import dev.ellesh.productpricinglisting.controllers.product.dto.ProductResponseDTO;
import dev.ellesh.productpricinglisting.controllers.product.dto.ProductRequestDTO;

import dev.ellesh.productpricinglisting.models.Product;
import dev.ellesh.productpricinglisting.services.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO request) {
        Product product = productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getCategoryId(),
                request.getMaximumRetailPrice(),
                request.getImageUrl()
        );
        return ResponseEntity.ok(toResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(toResponse(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequestDTO request) {
        Product product = productService.updateProduct(id);
        // TODO: update fields from request if needed
        return ResponseEntity.ok(toResponse(product));
    }

    private ProductResponseDTO toResponse(Product product) {
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory().getId());
        response.setMaximumRetailPrice(product.getMaximumRetailPrice());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}
