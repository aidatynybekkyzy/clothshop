package com.aidatynybekkyzy.clothshop.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends AbstractEntity<Long> {

    @Column(length = 50, nullable = false, unique = true)
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "categoryId")
    @ToString.Exclude
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName) && Objects.equals(products, category.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categoryName, products);
    }
}
