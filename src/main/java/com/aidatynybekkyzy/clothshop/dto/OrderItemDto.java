package com.aidatynybekkyzy.clothshop.dto;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@Builder @NoArgsConstructor
public class OrderItemDto {
    @NotNull
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private int quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=4, fraction=2)
    private BigDecimal sellingPrice;

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", sellingPrice=" + sellingPrice +
                '}';
    }
}
