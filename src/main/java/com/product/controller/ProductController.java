package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.product.entity.Product;
import com.product.services.ProductServices;

@Controller
public class ProductController {
	
	@Autowired
    ProductServices productService;

    @GetMapping("/")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index"; // Thymeleaf template (index.html)
    }
	@GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "error"; 
        }
        model.addAttribute("product",product);
		return "product";
      
    }
    @PostMapping("/product")
    public String addProduct(@RequestBody Product product, Model model) {
        productService.saveProduct(product);  // saveProduct() works
        model.addAttribute("products", productService.getAllProducts());
        return "index";
        }
	 @PostMapping("/product/{id}")
	    public String buyProduct(@PathVariable int id, Model model) {
	        Product product = productService.getProductById(id);
	        if (product != null) {
	            model.addAttribute("message", "Successfully purchased " + product.getName());
	        } else {
	            model.addAttribute("message", "Product not found!");
	        }
	        return "purchase-confirmation"; 
	    }
	
    @PutMapping("product/{id}")
	public ResponseEntity<Product> updateproduct(@PathVariable("id") int id, @RequestBody Product product){
		
		Product p = product.updateproduct(id, product);
		return new ResponseEntity<Product>(p, HttpStatus.OK);
	}
}