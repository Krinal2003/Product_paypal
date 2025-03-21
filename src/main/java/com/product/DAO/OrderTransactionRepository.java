package com.product.DAO;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.OrderTransaction;

@Repository
public interface OrderTransactionRepository  extends JpaRepository<OrderTransaction, Long> {
    Optional<OrderTransaction> findByTransactionId(String transactionId);
}
