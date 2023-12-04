package com.aidatynybekkyzy.clothshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    @NotNull
    private Long id;
    @NotEmpty @NotNull
    @Size(min = 4, max = 100)
    private String name;
    @NotNull @Positive
    @DecimalMin(value = "0.0", inclusive = false)
    @Positive(message = "Price must be greater than 0")
    @Digits(integer=4, fraction=2)
    private BigDecimal price;
    @NotNull @NotEmpty
    private Integer quantity;
    @NotNull @NotEmpty
    private Long categoryId;
    @NotNull @NotEmpty
    private Long vendorId;

    public ProductDto(long id, String name, BigDecimal price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
