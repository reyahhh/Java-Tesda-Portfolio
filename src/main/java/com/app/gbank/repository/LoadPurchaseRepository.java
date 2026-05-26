package com.app.gbank.repository;

import com.app.gbank.entity.LoadPurchase;
import com.app.gbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadPurchaseRepository extends JpaRepository<LoadPurchase, Long> {
    List<LoadPurchase> findAllByUserOrderByPurchasedAtDesc(User user);
}