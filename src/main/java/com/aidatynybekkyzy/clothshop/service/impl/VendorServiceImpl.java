package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.ProductAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.VendorAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.VendorNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.VendorMapper;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.model.Vendor;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.VendorService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    @Cacheable(value = "vendorsCache")
    public List<VendorDto> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream()
                .map(vendorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "vendorsCache", allEntries = true)
    public VendorDto createVendor(VendorDto vendorDto) {
        if (vendorRepository.existsByVendorName(vendorDto.getVendorName())) {
            throw new VendorAlreadyExistsException("Vendor with this name already exists ");
        }
        Vendor vendor = vendorMapper.toEntity(vendorDto);
        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(savedVendor);
    }

    @Override
    @CacheEvict(value = "vendorsCache", key = "#id")
    public VendorDto updateVendor(Long id, VendorDto vendorDto) {
        Vendor vendorExisting = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + vendorDto.getId()));

        if (vendorDto.getVendorName() == null || vendorDto.getVendorName().isEmpty()) {
            throw new InvalidArgumentException("Vendor name is required! ");
        }
        vendorExisting.setVendorName(vendorDto.getVendorName());

        Vendor updatedVendor = vendorRepository.save(vendorExisting);
        return vendorMapper.toDto(updatedVendor);
    }

    @Override
    @Cacheable(value = "vendorsCache", key = "#id")
    public VendorDto getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        return vendorMapper.toDto(vendor);
    }

    @Override
    @CacheEvict(value = "vendorsCache", key = "#id")
    public void deleteVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        vendorRepository.delete(vendor);
    }

    @Override
    @CacheEvict(value = "vendorsCache", key = "#id")
    public VendorDto addProductToVendor(long id, ProductDto productDto) throws ProductAlreadyExistsException {
        Vendor vendor = vendorMapper.toEntity(getVendorById(id));

        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new InvalidArgumentException("Product name is required! ");
        }
        if (vendor.getProducts().stream().anyMatch(p -> p.getName().equalsIgnoreCase(productDto.getName()))) {
            throw new ProductAlreadyExistsException("Product with the same name already exists for this vendor");
        }

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .categoryId(productDto.getCategoryId())
                .vendorId(productDto.getVendorId())
                .build();

        List<Product> products = vendor.getProducts();
        products.add(product);
        vendor.setProducts(products);

        vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    @Override
    @CacheEvict(value = "vendorProductsCache", key = "#id")
    public List<ProductDto> getVendorProducts(long id) {
        Vendor vendor = vendorMapper.toEntity(getVendorById(id));
        return vendorMapper.toProductDtoList(vendor.getProducts());
    }
}
