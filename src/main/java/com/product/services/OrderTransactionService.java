package com.product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.DAO.OrderTransactionRepository;
import com.product.entity.OrderTransaction;

import java.util.List;
import java.util.Optional;

@Service
public class OrderTransactionService {

    @Autowired
    private OrderTransactionRepository orderTransactionRepository;

    public List<OrderTransaction> getAllOrders() {
        return orderTransactionRepository.findAll();
    }

    public OrderTransaction saveTransaction(OrderTransaction transaction) {
        return orderTransactionRepository.save(transaction);
    }

    public Optional<OrderTransaction> findByTransactionId(String transactionId) {
        return orderTransactionRepository.findByTransactionId(transactionId);
    }
}