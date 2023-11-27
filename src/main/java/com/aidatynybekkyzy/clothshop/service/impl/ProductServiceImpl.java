package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.*;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.model.Vendor;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final VendorRepository vendorRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository,
                              VendorRepository vendorRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    @CacheEvict(value = "productsCache", allEntries = true, key = "#name")
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Saving new Product: " + productDto);
        if (Objects.equals(productDto.getName(), "") || productDto.getName().isEmpty()) {
            log.error("Product name is empty or null " + productDto + "error: " + InvalidArgumentException.class.getName());
            throw new InvalidArgumentException("Product name is required ");
        }
        if (productRepository.existsByName(productDto.getName())) {
            log.error("Product with name: " + productDto.getName() + " already exists. Error: " + EntityAlreadyExistsException.class);
            throw new EntityAlreadyExistsException("Product with this name already exists " + productDto.getName());
        }
        Vendor vendor = vendorRepository.findById(productDto.getVendorId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + productDto.getVendorId()));

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .categoryId(productDto.getCategoryId())
                .vendorId(vendor.getId())
                .build();
        productRepository.save(product);
        log.info("New Product saved: " + product);
        return productMapper.toDto(product);
    }

    @Override
    @Cacheable(value = "productsCache", key = "#id")
    @Transactional
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Cacheable(value = "productsCache")
    @Transactional
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "productsCache", key = "#id")
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDTO) {
        log.info("Product to update: " + productDTO);
        Product existingProduct = productMapper.toEntity(getProductById(id));

        if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
            log.error("Product name is null or empty + " + productDTO.getName());
            throw new InvalidArgumentException(" Product name is required ");
        }

        Product updatedProduct = productMapper.toEntity(productDTO);
        updatedProduct.setId(existingProduct.getId());

        Product savedProduct = productRepository.save(updatedProduct);
        log.info("Updated and saved product: " + savedProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @CacheEvict(value = "productsCache", key = "#id")
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public byte[] getProductPhoto(Long productId) {
        log.info("Getting photo from product SERVICE");
        Product product = productMapper.toEntity(getProductById(productId));
        return product.getPhoto();
    }

    @Override
    @Transactional
    public void addPhoto(Long id, byte[] photo) {
        log.info("Adding photo to product SERVICE");
        Product product = productMapper.toEntity(getProductById(id));
        product.setPhoto(photo);
        productRepository.save(product);
    }
}
