package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.CategoryDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.CategoryAlreadyExistsException;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto) throws CategoryAlreadyExistsException;
    List<ProductDto> getAllProductsByCategoryId(Long categoryId);
    CategoryDto getCategoryById(Long id);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);

}
