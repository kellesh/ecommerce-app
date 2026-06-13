package dev.ellesh.productpricinglisting.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
public class Category extends BaseModel {
    private String title;
}
