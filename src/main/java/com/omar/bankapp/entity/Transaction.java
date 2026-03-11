package com.omar.bankapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a financial transaction
 * performed on a bank account.
 *
 * Each transaction belongs to exactly one account.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many transactions can belong to one bank account.
     * Lazy loading is used to improve performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    /**
     * Type of transaction (DEPOSIT or WITHDRAW).
     */
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * Amount involved in the transaction.
     */
    private double amount;

    /**
     * Timestamp indicating when the transaction occurred.
     */
    private LocalDateTime createdAt;

    public Transaction() {
    }

    /**
     * Constructor used when creating a new transaction record.
     */
    public Transaction(BankAccount account, TransactionType type, double amount) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public BankAccount getAccount() {
        return account;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}