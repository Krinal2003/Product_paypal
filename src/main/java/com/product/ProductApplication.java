package com.product;

import com.product.DAO.ProductDAO;
import com.product.entity.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.product")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(ProductDAO productRepository) {
        return args -> {
            if (productRepository.count() == 0) {  // Avoid duplicate entries
                productRepository.save(new Product("Laptop", "High-performance laptop", 10, 750.0, "laptop.jpg"));
                productRepository.save(new Product( "Phone", "Latest smartphone", 20, 500.0, "phone.jpg"));
                productRepository.save(new Product( "Headphones", "Noise-canceling headphones", 15, 2000.0, "Airpods.jpg"));
                productRepository.save(new Product("Smartwatch", "Feature-packed smartwatch", 30, 800.0, "watch.jpg"));
            }
        };
    }
}
