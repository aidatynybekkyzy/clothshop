package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.ProductNotFoundException;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            ProductDto productDto = productService.getProductById(id);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDto);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productId}/photo")
    public ResponseEntity<byte[]> getProductPhoto(@PathVariable Long productId) {
        try {
            byte[] photo = productService.getProductPhoto(productId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().body("Product deleted successfully");
        } catch (ProductNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return handleException(e, HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> addPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            byte[] photoBytes = photo.getBytes();
            productService.addPhoto(id, photoBytes);
            return ResponseEntity.ok("Photo added successfully");
        } catch (ProductNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding photo");
        }
    }

    private ResponseEntity<ApiResponse> handleException(Exception exception, HttpStatus status) {
        ApiResponse errorResponse = new ApiResponse(status.value(), "Error", exception.getMessage());
        return ResponseEntity.status(status).body(errorResponse);

    }

}
