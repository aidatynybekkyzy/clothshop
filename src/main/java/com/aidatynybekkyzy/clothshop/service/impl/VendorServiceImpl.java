package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.VendorAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.VendorNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.mapper.VendorMapper;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.model.Vendor;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final ProductMapper productMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper, ProductMapper productMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<VendorDto> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream()
                .map(vendorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VendorDto createVendor(VendorDto vendorDto) {
        if (vendorRepository.existsByVendorName(vendorDto.getVendorName())) {
            throw new VendorAlreadyExistsException("Vendor with this name already exists ");
        }
        Vendor vendor = vendorMapper.toEntity(vendorDto);
        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(savedVendor);
    }

    @Override
    public VendorDto updateVendor(Long id, VendorDto vendorDto) {
        Vendor vendorExisting = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + vendorDto.getVendorId()));

        if (vendorDto.getVendorName() == null || vendorDto.getVendorName().isEmpty()) {
            throw new InvalidArgumentException("Vendor name is required! ");
        }
        vendorExisting.setVendorName(vendorDto.getVendorName());

        Vendor updatedVendor = vendorRepository.save(vendorExisting);
        return vendorMapper.toDto(updatedVendor);
    }

    @Override
    public VendorDto getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        return vendorMapper.toDto(vendor);
    }

    @Override
    public void deleteVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        vendorRepository.delete(vendor);
    }

    @Override //todo add exception ProductAlready exists
    public VendorDto addProductToVendor(long id, ProductDto productDto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new InvalidArgumentException("Product name is required! ");
        }
        Product product = productMapper.toEntity(productDto);
        vendor.getProducts().add(product);

        Vendor updatedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(updatedVendor);
    }

    @Override
    public List<ProductDto> getVendorProducts(long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        List<ProductDto> products = vendor.getProducts().stream()
                .map(productMapper::toDto)
                .toList();
        return products;
    }
}
