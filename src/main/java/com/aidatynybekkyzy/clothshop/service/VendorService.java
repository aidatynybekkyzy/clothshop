package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.exception.ProductAlreadyExistsException;

import java.util.List;

public interface VendorService {
    List<VendorDto> getAllVendors();

    VendorDto createVendor(VendorDto vendorDto);
    VendorDto updateVendor(Long id, VendorDto vendorDto);
    VendorDto getVendorById(Long id);

    void deleteVendorById(Long id);

    VendorDto addProductToVendor(long id, ProductDto productDto) throws ProductAlreadyExistsException;

    List<ProductDto> getVendorProducts(long id);

}
