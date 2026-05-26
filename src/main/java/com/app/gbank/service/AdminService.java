package com.app.gbank.service;

import com.app.gbank.entity.AuditLog;
import com.app.gbank.entity.Transaction;
import com.app.gbank.entity.User;
import com.app.gbank.repository.AuditLogRepository;
import com.app.gbank.repository.TransactionRepository;
import com.app.gbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public long countTransactions() {
        return transactionRepository.count();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalBalanceAllUsers() {
        return transactionRepository.sumAllBalances();
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop10ByOrderByPerformedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getUserAuditLogs(User user) {
        return auditLogRepository.findAllByActorEmailOrderByPerformedAtDesc(user.getEmail());
    }

    @Transactional
    public void log(String actorEmail, String action, String details) {
        AuditLog log = new AuditLog();
        log.setActorEmail(actorEmail);
        log.setAction(action);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}