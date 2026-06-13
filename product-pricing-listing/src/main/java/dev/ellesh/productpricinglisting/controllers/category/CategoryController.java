package dev.ellesh.productpricinglisting.controllers.category;

import dev.ellesh.productpricinglisting.controllers.category.dto.CategoryRequestDTO;
import dev.ellesh.productpricinglisting.controllers.category.dto.CategoryResponseDTO;
import dev.ellesh.productpricinglisting.models.Category;
import dev.ellesh.productpricinglisting.services.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> responses = categoryService.getAllCategories()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // GET category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(toResponse(category));
    }

    // POST create category
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO request) {
        Category category = categoryService.createCategory(request.getTitle());
        return ResponseEntity.ok(toResponse(category));
    }

    // PUT update category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id,
                                                              @RequestBody CategoryRequestDTO request) {
        Category category = categoryService.updateCategory(id, request.getTitle());
        return ResponseEntity.ok(toResponse(category));
    }

    // DELETE category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // Mapper
    private CategoryResponseDTO toResponse(Category category) {
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(category.getId());
        response.setTitle(category.getTitle());
        return response;
    }
}
