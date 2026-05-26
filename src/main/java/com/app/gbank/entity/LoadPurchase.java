package com.app.gbank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "load_purchases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoadPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 11)
    private String targetMobileNumber;

    @Column(nullable = false)
    private String network;   // GLOBE, SMART, TNT, DITO, SUN

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 100)
    private String referenceNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime purchasedAt;
}