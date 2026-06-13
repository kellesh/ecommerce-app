package dev.ellesh.productpricinglisting.models;


import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseModel {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
