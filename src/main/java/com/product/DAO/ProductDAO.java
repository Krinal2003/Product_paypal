package com.product.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product,Integer>
{
//		public List<Product>findByuser(String name);
//		
//		@Query("SELECT p FROM Product p WHERE p.name = :name")
//	    List<Product> findByUser(@Param("name") String name);
		
}
