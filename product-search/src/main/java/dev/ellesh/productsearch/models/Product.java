package dev.ellesh.productsearch.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Data
@Document(indexName = "products")
public class Product {
    @Id
    private String id;

    private String name;
    private String description;

    // Filtering field
    private Long categoryId;

    // Text search field
    private String categoryName;
    
    private double price;


}
