package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }

    @PostMapping
    public ResponseEntity<VendorDto> createVendor(@RequestBody VendorDto vendorDto) {
        VendorDto createdVendor = vendorService.createVendor(vendorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VendorDto> updateVendor(@PathVariable Long id, @RequestBody VendorDto vendorDto) {
        VendorDto updatedVendor = vendorService.updateVendor(id, vendorDto);
        return ResponseEntity.ok(updatedVendor);

    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable Long id) {
        VendorDto vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendor);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteVendorById(@PathVariable Long id) {
        vendorService.deleteVendorById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/{id}/products")
    public ResponseEntity<VendorDto> addProductToVendor(@PathVariable long id, @RequestBody ProductDto productDto) {
        VendorDto updatedVendor = vendorService.addProductToVendor(id, productDto);
        return ResponseEntity.ok(updatedVendor);

    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDto>> getVendorProducts(@PathVariable long id) {
        List<ProductDto> products = vendorService.getVendorProducts(id);
        return ResponseEntity.ok(products);
    }
}
