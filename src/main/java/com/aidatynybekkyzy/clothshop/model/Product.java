package com.aidatynybekkyzy.clothshop.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

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
}
