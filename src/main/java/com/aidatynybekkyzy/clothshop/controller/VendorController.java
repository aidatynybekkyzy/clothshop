package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.exception.ProductAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.VendorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vendors")
@Api("Vendor controller")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    @ApiOperation("Get the list of all vendors")
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }

    @PostMapping("/admin/createVendor")
    @ApiOperation("Creating new vendor")
    public ResponseEntity<VendorDto> createVendor(@RequestBody @Valid VendorDto vendorDto) {
        VendorDto createdVendor = vendorService.createVendor(vendorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
    }

    @PatchMapping("/admin/{id}")
    @ApiOperation("Updating the vendor")
    public ResponseEntity<VendorDto> updateVendor(@PathVariable Long id, @RequestBody @Valid VendorDto vendorDto) {
        VendorDto updatedVendor = vendorService.updateVendor(id, vendorDto);
        return ResponseEntity.ok(updatedVendor);

    }

    @GetMapping("/{id}")
    @ApiOperation("Getting the vendor by id")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable Long id) {
        VendorDto vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendor);

    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Deleting the vendor by id")
    public ResponseEntity<ApiResponse> deleteVendorById(@PathVariable Long id) {
        vendorService.deleteVendorById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/admin/{id}/products")
    @ApiOperation("Adding product to a vendor")
    public ResponseEntity<VendorDto> addProductToVendor(@PathVariable long id, @RequestBody @Valid ProductDto productDto) throws ProductAlreadyExistsException {
        VendorDto updatedVendor = vendorService.addProductToVendor(id, productDto);
        return ResponseEntity.ok(updatedVendor);
    }

    @GetMapping("/{id}/products") //todo add endpoint to spring security
    @ApiOperation("Getting list of all vendor products")
    public ResponseEntity<List<ProductDto>> getVendorProducts(@PathVariable long id) {
        List<ProductDto> products = vendorService.getVendorProducts(id);
        return ResponseEntity.ok(products);
    }
}
