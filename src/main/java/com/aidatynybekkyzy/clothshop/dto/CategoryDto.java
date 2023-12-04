package com.aidatynybekkyzy.clothshop.dto;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @Null
    private Long id;

    @NotEmpty() @NotBlank
    @Size(min = 4, max = 50)
    private String categoryName;

    private List<ProductDto> products;
}
