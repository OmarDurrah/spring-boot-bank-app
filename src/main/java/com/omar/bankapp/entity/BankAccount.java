package com.omar.bankapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity representing a bank account.
 *
 * Each account belongs to a single user and
 * stores the account balance.
 */
@Entity
@Table(name = "accounts")
public class BankAccount {

    /**
     * Unique identifier for the account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Current account balance.
     */
    private double balance;

    /**
     * Version field used for Optimistic Locking.
     *
     * Prevents concurrent updates from overwriting each other
     * when multiple transactions attempt to modify the balance.
     */
    @Version
    private Long version;

    /**
     * Many accounts belong to one user.
     * Hidden from JSON responses to prevent circular serialization.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public BankAccount() {}

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Deposit money into the account.
     *
     * @param amount amount to add
     * @throws IllegalArgumentException if amount is not positive
     */
    public void deposit(double amount) {

        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        this.balance += amount;
    }

    /**
     * Withdraw money from the account.
     *
     * @param amount amount to withdraw
     * @return true if withdrawal succeeded, false if insufficient balance
     */
    public boolean withdraw(double amount) {

        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        if (amount > balance)
            return false;

        this.balance -= amount;

        return true;
    }

    /**
     * Assign the account to a specific user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieve account owner.
     */
    public User getUser() {
        return user;
    }
}