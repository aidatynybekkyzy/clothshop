package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.ProductAlreadyExistsException;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto) throws ProductAlreadyExistsException;
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(Long id, ProductDto productDTO);
    void deleteProduct(Long id);

    byte[] getProductPhoto(Long productId);
    void addPhoto(Long id, byte [] photo);
}
