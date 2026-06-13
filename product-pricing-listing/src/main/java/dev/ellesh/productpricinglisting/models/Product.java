package dev.ellesh.productpricinglisting.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Product extends BaseModel {

    private String name;
    private String description;

    @ManyToOne
    private Category category;

    private BigDecimal maximumRetailPrice;

    private String imageUrl;

}
