package com.aidatynybekkyzy.clothshop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;

    @Column(length = 100, nullable = false)
    private String vendorName;

    @OneToMany(fetch = FetchType.LAZY/*, mappedBy = "vendor"*/, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Product> products;

    @Tolerate
    public Vendor(Long vendorId, String vendorName) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
    }

    @Tolerate
    public Vendor(Long vendorId, String vendorName, List<Product> products) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vendor vendor = (Vendor) o;
        return vendorId != null && Objects.equals(vendorId, vendor.vendorId) &&
                vendorName != null && Objects.equals(vendorName, vendor.vendorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, vendorName);
    }
}
