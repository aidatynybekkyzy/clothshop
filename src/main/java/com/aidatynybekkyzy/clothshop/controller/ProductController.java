package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.ProductAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
@Api("Product controller")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Creating new product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) throws ProductAlreadyExistsException {
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation("Getting the product by Id")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.info("CONTROLLER  Get product by id");
        ProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("Getting list of all products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Updating the product")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{productId}/photo") // todo добавить эндпоинт в секьюрити
    @ApiOperation("Getting the photo of a product by productId")
    public ResponseEntity<byte[]> getProductPhoto(@PathVariable Long productId) {
        byte[] photo = productService.getProductPhoto(productId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Deleting the product by id")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/photo")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Adding photo to a product")
    public ResponseEntity<?> addPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) throws IOException {
        byte[] photoBytes = photo.getBytes();
        productService.addPhoto(id, photoBytes);
        return ResponseEntity.ok("Photo added successfully");
    }
}
