package com.product.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "order_transaction")
public class OrderTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    
    public OrderTransaction(Product product, String transactionId, double amount, String currency, String status) {
        this.product = product;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.purchaseDate = LocalDateTime.now();
    }

  
    public OrderTransaction() {
    	
    }
    public Long getId() { 
    	return id; }
    public void setId(Long id) {
    	this.id = id; }

    public Product getProduct() { 
    	return product; }
    public void setProduct(Product product) {
    	this.product = product; }

    public String getTransactionId() {
    	return transactionId; }
    public void setTransactionId(String transactionId) { 
    	this.transactionId = transactionId; }

    public double getAmount() { 
    	return amount; }
    public void setAmount(double amount) { 
    	this.amount = amount; }

    public String getCurrency() { 
    	return currency; }
    public void setCurrency(String currency) { 
    	this.currency = currency; }

    public String getStatus() { 
    	return status; }
    public void setStatus(String status) { 
    	this.status = status; }

    public LocalDateTime getPurchaseDate() { 
    	return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { 
    	this.purchaseDate = purchaseDate; }
}
