package com.aidatynybekkyzy.clothshop.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Arrays;

@Entity
@Getter
@Setter
@Table(name = "products")
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AbstractEntity<Long> {

    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    @Positive(message = "Price must be greater than 0")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    private Integer quantity;
    @Column(name = "category_id")
    private Long categoryId;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "vendor_id", nullable = false)
    @Column(name = "vendor_id")
    private Long vendorId;
    @Column(name = "product_photo")
    private byte[] photo;

    public Product(Long id, String name, BigDecimal price, Integer quantity, Long category, Long vendor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = category;
        this.vendorId = vendor;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", categoryId=" + categoryId +
                ", vendorId=" + vendorId +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
