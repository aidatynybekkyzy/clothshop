package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.CategoryDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.CategoryAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.CategoryNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDTO)  {
        log.info("Creating new category");
        try {
            CategoryDto createdCategory = categoryService.createCategory(categoryDTO);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (InvalidArgumentException exception) {
            return handleException(exception, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        try {
            CategoryDto categoryDTO = categoryService.getCategoryById(id);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CategoryNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategoryId(@PathVariable Long categoryId) {
        try {
            List<ProductDto> products = categoryService.getAllProductsByCategoryId(categoryId);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private ResponseEntity<ApiResponse> handleException(Exception exception, HttpStatus status) {
        ApiResponse errorResponse = new ApiResponse(status.value(), "Error", exception.getMessage());
        return ResponseEntity.status(status).body(errorResponse);

    }
}
