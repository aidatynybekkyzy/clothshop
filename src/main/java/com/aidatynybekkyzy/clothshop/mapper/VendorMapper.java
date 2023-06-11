package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.model.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    @Mapping(source = "vendorName", target = "vendorName")
    VendorDto toDto(Vendor vendor);

    @Mapping(source = "vendorName", target = "vendorName")
    Vendor toEntity(VendorDto vendorDto);

    List<ProductDto> toProductDtoList(List<Product> orderItems);
}
