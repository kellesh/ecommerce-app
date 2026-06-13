package dev.ellesh.productpricinglisting.controllers.product.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private Long categoryId;
    private BigDecimal maximumRetailPrice;
    private String imageUrl;
}
