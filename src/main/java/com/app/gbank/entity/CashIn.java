package com.app.gbank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_ins")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CashIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String source;   // e.g. "Bank Transfer", "Over-the-Counter"

    @Column(length = 100)
    private String referenceNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime cashedInAt;
}