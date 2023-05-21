package com.aidatynybekkyzy.clothshop.loader;

import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

    }

    /*private void loadProducts() {
        Product product = Product.builder()
                .id(1L)
                .name("KIA")
                .price(BigDecimal.valueOf(100_000))
                .quantity(1)
                .vendorId(1L)
                .build();
    }*/
}
