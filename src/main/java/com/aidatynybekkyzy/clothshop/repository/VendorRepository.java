package com.aidatynybekkyzy.clothshop.repository;

import com.aidatynybekkyzy.clothshop.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository  extends JpaRepository<Vendor, Long> {
    boolean existsByVendorName(String name);
}
