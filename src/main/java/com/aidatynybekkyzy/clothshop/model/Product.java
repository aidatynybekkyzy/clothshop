package com.aidatynybekkyzy.clothshop.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "products")
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    //@Lob //для хранения больших данных создается отдельный столбец Large Object

    private Integer quantity;
    @Column(name="category_id", nullable = false)
    private Long categoryId;

    @Column(name="vendor_id", nullable = false)
    private Long vendorId;
    @Column(name = "product_photo")
    private byte[] photo;

    public Product(Long id, String name, BigDecimal price, Integer quantity, Long categoryId, Long vendorId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.vendorId = vendorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(quantity, product.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, photo, quantity);
    }
}
