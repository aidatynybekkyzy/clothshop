package com.aidatynybekkyzy.clothshop.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
}
