package com.aidatynybekkyzy.clothshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.aidatynybekkyzy.clothshop.model")
public class ClothShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothShopApplication.class, args);
	}

}
