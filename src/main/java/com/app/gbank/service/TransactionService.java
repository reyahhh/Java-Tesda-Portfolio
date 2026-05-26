package com.app.gbank.service;

import com.app.gbank.dto.*;
import com.app.gbank.entity.*;
import com.app.gbank.enums.TransactionType;
import com.app.gbank.exception.InsufficientBalanceException;
import com.app.gbank.exception.UserNotFoundException;
import com.app.gbank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BillPaymentRepository billPaymentRepository;
    private final LoadPurchaseRepository loadPurchaseRepository;

    @Transactional
    public Transaction sendMoney(User sender, SendMoneyRequest request) {
        if (sender.getMobileNumber().equals(request.recipientMobileNumber())) {
            throw new IllegalArgumentException("You cannot send money to yourself.");
        }

        User receiver = userRepository.findByMobileNumber(request.recipientMobileNumber())
                .orElseThrow(() -> new UserNotFoundException(
                        "Recipient not found: " + request.recipientMobileNumber()));

        if (sender.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: ₱" + sender.getBalance());
        }

        sender.setBalance(sender.getBalance().subtract(request.amount()));
        receiver.setBalance(receiver.getBalance().add(request.amount()));
        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction tx = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .type(TransactionType.SEND)
                .amount(request.amount())
                .description(request.note())
                .referenceNumber(generateRef())
                .build();

        return transactionRepository.save(tx);
    }

    @Transactional
    public BillPayment payBill(User user, BillPaymentRequest request) {
        if (user.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: ₱" + user.getBalance());
        }

        user.setBalance(user.getBalance().subtract(request.amount()));
        userRepository.save(user);

        // Record as transaction
        Transaction tx = Transaction.builder()
                .sender(user)
                .type(TransactionType.BILL_PAYMENT)
                .amount(request.amount())
                .description("Bill: " + request.billerName() + " | Acct: " + request.accountNumber())
                .referenceNumber(generateRef())
                .build();
        transactionRepository.save(tx);

        BillPayment bill = BillPayment.builder()
                .user(user)
                .billerName(request.billerName())
                .accountNumber(request.accountNumber())
                .amount(request.amount())
                .referenceNumber(tx.getReferenceNumber())
                .build();

        return billPaymentRepository.save(bill);
    }

    @Transactional
    public LoadPurchase buyLoad(User user, LoadPurchaseRequest request) {
        if (user.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: ₱" + user.getBalance());
        }

        user.setBalance(user.getBalance().subtract(request.amount()));
        userRepository.save(user);

        Transaction tx = Transaction.builder()
                .sender(user)
                .type(TransactionType.LOAD_PURCHASE)
                .amount(request.amount())
                .description(request.network() + " Load → " + request.targetMobileNumber())
                .referenceNumber(generateRef())
                .build();
        transactionRepository.save(tx);

        LoadPurchase load = LoadPurchase.builder()
                .user(user)
                .targetMobileNumber(request.targetMobileNumber())
                .network(request.network())
                .amount(request.amount())
                .referenceNumber(tx.getReferenceNumber())
                .build();

        return loadPurchaseRepository.save(load);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(User user) {
        return transactionRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getRecentTransactions(User user) {
        return transactionRepository
                .findTop5BySenderOrReceiverOrderByCreatedAtDesc(user, user);
    }

    private String generateRef() {
        return "GC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
