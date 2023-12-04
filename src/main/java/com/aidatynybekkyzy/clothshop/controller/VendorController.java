package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.VendorService;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendors")
@Api("Vendor controller")
@PreAuthorize("hasAnyRole('ADMIN')")
public class VendorController {
    private final VendorService vendorService;
    private final ResponseErrorValidation responseErrorValidation;


    @GetMapping
    @ApiOperation("Get the list of all vendors")
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }

    @PostMapping("/createVendor")
    @ApiOperation("Creating new vendor")
    public ResponseEntity<?> createVendor(@RequestBody @Valid VendorDto vendorDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        VendorDto createdVendor = vendorService.createVendor(vendorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Updating the vendor")
    public ResponseEntity<?> updateVendor(@PathVariable Long id, @RequestBody @Valid VendorDto vendorDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        VendorDto updatedVendor = vendorService.updateVendor(id, vendorDto);
        return ResponseEntity.ok(updatedVendor);

    }

    @GetMapping("/{id}")
    @ApiOperation("Getting the vendor by id")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable Long id) {
        VendorDto vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendor);

    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deleting the vendor by id")
    public ResponseEntity<ApiResponse> deleteVendorById(@PathVariable Long id) {
        vendorService.deleteVendorById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/{id}/products")
    @ApiOperation("Adding product to a vendor")
    public ResponseEntity<?> addProductToVendor(@PathVariable long id, @RequestBody @Valid ProductDto productDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        VendorDto updatedVendor = vendorService.addProductToVendor(id, productDto);
        return ResponseEntity.ok(updatedVendor);
    }

    @GetMapping("/{id}/products")
    @ApiOperation("Getting list of all vendor products")
    public ResponseEntity<List<ProductDto>> getVendorProducts(@PathVariable long id) {
        List<ProductDto> products = vendorService.getVendorProducts(id);
        return ResponseEntity.ok(products);
    }
}
