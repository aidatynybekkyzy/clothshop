package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.CategoryDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.CategoryAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.CategoryNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.mapper.CategoryMapper;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.model.Category;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.repository.CategoryRepository;
import com.aidatynybekkyzy.clothshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override //todo categoryId returns null instead of actualID
    public CategoryDto createCategory(CategoryDto categoryDto) throws CategoryAlreadyExistsException {
            Category category = categoryMapper.toEntity(categoryDto);
            if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
                throw new InvalidArgumentException("Category name is required");
            }
            if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
                throw new CategoryAlreadyExistsException("Category already exists with name: " + category.getCategoryName());
            }
            Category savedCategory = categoryRepository.save(category);
            return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<ProductDto> getAllProductsByCategoryId(Long categoryId) {
        Category category = categoryMapper.toEntity(getCategoryById(categoryId));

        List<Product> products = category.getProducts();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(getCategoryById(id));

        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isEmpty()) {
            throw new InvalidArgumentException("Category name is required");
        }

        category.setCategoryName(categoryDto.getCategoryName());
        /*category.setProducts(categoryDto.getProducts().stream()
                .map(productMapper::toEntity)
                .collect(Collectors.toList()));*/

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
