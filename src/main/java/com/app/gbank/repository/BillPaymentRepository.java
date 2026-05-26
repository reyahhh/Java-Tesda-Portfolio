package com.app.gbank.repository;

import com.app.gbank.entity.BillPayment;
import com.app.gbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    List<BillPayment> findAllByUserOrderByPaidAtDesc(User user);
}