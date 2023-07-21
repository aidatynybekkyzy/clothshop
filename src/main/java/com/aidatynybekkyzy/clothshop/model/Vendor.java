package com.aidatynybekkyzy.clothshop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
public class Vendor extends AbstractEntity<Long> {

    @Column(length = 100, nullable = false)
    private String vendorName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendorId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Product> products;

    @Tolerate
    public Vendor(Long id, String vendorName) {
        this.id = id;
        this.vendorName = vendorName;
    }

    @Tolerate
    public Vendor(Long id, String vendorName, List<Product> products) {
        this.id = id;
        this.vendorName = vendorName;
        this.products = products;
    }
}
