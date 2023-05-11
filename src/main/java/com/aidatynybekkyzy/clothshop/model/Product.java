package com.aidatynybekkyzy.clothshop.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] photo;
    private Integer quantity;
    @Column(name="category_id", nullable = false)
    private Long categoryId;
    @Column(name="vendor_id", nullable = false)
    private Long vendorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(quantity, product.quantity) &&
                Objects.equals(categoryId, product.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity, categoryId);
    }
}
