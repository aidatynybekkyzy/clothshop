package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.service.ProductService;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Api("Product controller")
public class ProductController {
    private final ProductService productService;
    private final ResponseErrorValidation responseErrorValidation;


    @PostMapping("/createProduct")
    @ApiOperation("Creating new product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation("Getting the product by Id")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.info("Get product by id: {}", id);
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
    @ApiOperation("Updating the product")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto productDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
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
    @ApiOperation("Deleting the product by id")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/photo")
    @ApiOperation("Adding photo to a product")
    public ResponseEntity<?> addPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) throws IOException {
        byte[] photoBytes = photo.getBytes();
        productService.addPhoto(id, photoBytes);
        return ResponseEntity.ok("Photo added successfully");
    }
}
