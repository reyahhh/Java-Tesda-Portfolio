package com.app.gbank.repository;

import java.math.BigDecimal;
import java.util.List;

import com.app.gbank.entity.Transaction;
import com.app.gbank.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sender = :user OR t.receiver = :user ORDER BY t.createdAt DESC")
    List<Transaction> findAllByUser(User user);

    @Query("SELECT t FROM Transaction t WHERE t.sender = :user OR t.receiver = :user ORDER BY t.createdAt DESC")
    Page<Transaction> findAllByUserPaged(User user, Pageable pageable);

    List<Transaction> findTop5BySenderOrReceiverOrderByCreatedAtDesc(User sender, User receiver);

    @Query("SELECT COALESCE(SUM(u.balance), 0) FROM User u")
    BigDecimal sumAllBalances();
}