package com.aidatynybekkyzy.clothshop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
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
    @Null()
    @NotNull()
    private Long categoryId;

    @NotEmpty()
    @Size(min = 4, max = 50)
    private String categoryName;

    private List<ProductDto> products;
}
