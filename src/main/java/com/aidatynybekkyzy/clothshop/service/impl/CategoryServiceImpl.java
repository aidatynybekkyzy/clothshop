package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.CategoryDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.EntityAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.EntityNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.mapper.CategoryMapper;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.model.Category;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.repository.CategoryRepository;
import com.aidatynybekkyzy.clothshop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isEmpty()) {
            throw new InvalidArgumentException("Category name is required");
        }
        Category category = categoryMapper.toEntity(categoryDto);

        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new EntityAlreadyExistsException("Category already exists with name: " + category.getCategoryName());
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @CacheEvict(value = "productsCaсhe", key = "#categoryId")
    @Transactional
    public List<ProductDto> getAllProductsByCategoryId(Long categoryId) {
        Category category = categoryMapper.toEntity(getCategoryById(categoryId));

        List<Product> products = category.getProducts();
        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    @Cacheable(value = "categoriesCache")
    @Transactional
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "categoriesCache", key = "#id")
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        log.info("Updating category with id: " + id + " " + categoryDto.toString());
        Category category = categoryMapper.toEntity(getCategoryById(id));

        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isEmpty()) {
            throw new InvalidArgumentException("Category name is required");
        }

        category.setCategoryName(categoryDto.getCategoryName());

        Category updatedCategory = categoryRepository.save(category);
        log.info("updated Category: " + updatedCategory.toString());
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @CacheEvict(value = "categoriesCache", key = "#id")
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
