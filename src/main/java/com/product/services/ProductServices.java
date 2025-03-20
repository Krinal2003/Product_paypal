package com.product.services;

import com.product.DAO.ProductDAO;
//import com.product.DAO.productDAO;
import com.product.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {
   
	@Autowired
	ProductDAO productRepository;

    // Constructor Injection
    public ProductServices(ProductDAO productRepository) {
        this.productRepository = productRepository;
    }
   
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

	public static Product getPaymentById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	}

