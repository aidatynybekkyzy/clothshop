package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.VendorAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.VendorNotFoundException;
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
    public ResponseEntity<?> createVendor(@RequestBody VendorDto vendorDto) {
        try {
            VendorDto createdVendor = vendorService.createVendor(vendorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
        } catch (VendorAlreadyExistsException exception) {
            return handleException(exception, HttpStatus.CONFLICT);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateVendor(@PathVariable Long id, @RequestBody VendorDto vendorDto) {
        try {
            VendorDto updatedVendor = vendorService.updateVendor(id,vendorDto);
            return ResponseEntity.ok(updatedVendor);
        } catch (VendorNotFoundException exception) {
            return handleException(exception, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVendorById(@PathVariable Long id) {
        try {
            VendorDto vendor = vendorService.getVendorById(id);
            return ResponseEntity.ok(vendor);
        } catch (VendorNotFoundException exception) {
            return handleException(exception, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteVendorById(@PathVariable Long id) {
        try {
            vendorService.deleteVendorById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (VendorNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<?> addProductToVendor(@PathVariable long id, @RequestBody ProductDto productDto) {
        try {
            VendorDto updatedVendor = vendorService.addProductToVendor(id, productDto);
            return ResponseEntity.ok(updatedVendor);
        } catch (VendorNotFoundException exception) {
            return handleException(exception, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<?> getVendorProducts(@PathVariable long id) {
        try {
            List<ProductDto> products = vendorService.getVendorProducts(id);
            return ResponseEntity.ok(products);
        } catch (VendorNotFoundException exception) {
            return handleException(exception, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<ApiResponse> handleException(Exception exception, HttpStatus status) {
        ApiResponse errorResponse = new ApiResponse(status.value(), "Error", exception.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
