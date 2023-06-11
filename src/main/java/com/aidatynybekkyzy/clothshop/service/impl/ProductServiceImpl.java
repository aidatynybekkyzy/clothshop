package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.ProductAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.ProductNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.VendorNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.CategoryMapper;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.model.Vendor;
import com.aidatynybekkyzy.clothshop.repository.CategoryRepository;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.CategoryService;
import com.aidatynybekkyzy.clothshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public ProductDto createProduct(ProductDto productDto) throws ProductAlreadyExistsException {
        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new InvalidArgumentException("Product name is required ");
        }
        if (productRepository.existsByName(productDto.getName())) {
            throw new ProductAlreadyExistsException("Product with this name already exists " + productDto.getName());
        }
        Vendor vendor = vendorRepository.findById(productDto.getVendorId())
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with ID: " + productDto.getVendorId()));

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .categoryId(productDto.getCategoryId())
                .vendorId(productDto.getVendorId())
                .build();
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDTO) {
        Product existingProduct = productMapper.toEntity(getProductById(id));

        if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
            throw new InvalidArgumentException(" Product name is required ");
        }

        Product updatedProduct = productMapper.toEntity(productDTO);
        updatedProduct.setId(existingProduct.getId());

        Product savedProduct = productRepository.save(updatedProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public byte[] getProductPhoto(Long productId) {
        log.info("Getting photo from product SERVICE");
        Product product = productMapper.toEntity(getProductById(productId));
        return product.getPhoto();
    }

    @Override
    public void addPhoto(Long id, byte[] photo) {
        log.info("Adding photo to product SERVICE");
        Product product = productMapper.toEntity(getProductById(id));
        product.setPhoto(photo);
        productRepository.save(product);
    }
}
