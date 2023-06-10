package com.aidatynybekkyzy.clothshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotNull
    private Long id;
    @NotEmpty
    @Size(min = 4, max = 100)
    private String name;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=4, fraction=2)
    private BigDecimal price;
    private Integer quantity;
    @NotNull
    private Long categoryId;
    private Long vendorId;
}
