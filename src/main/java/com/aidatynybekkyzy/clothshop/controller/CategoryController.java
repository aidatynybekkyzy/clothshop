package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.CategoryDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.CategoryService;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
@Api("Category controller")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    @ApiOperation("Creating new category")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDto categoryDTO, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        CategoryDto createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation("Getting the category by id")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        CategoryDto categoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("Getting the list of categories")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Updating category")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deleting category")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{categoryId}/products")
    @ApiOperation("Getting all products of a category")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductDto> products = categoryService.getAllProductsByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
