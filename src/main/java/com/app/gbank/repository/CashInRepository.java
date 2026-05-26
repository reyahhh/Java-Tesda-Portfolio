package com.app.gbank.repository;

import com.app.gbank.entity.CashIn;
import com.app.gbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashInRepository extends JpaRepository<CashIn, Long> {
    List<CashIn> findAllByUserOrderByCashedInAtDesc(User user);
}