package com.omar.bankapp.dto;

public class AccountRequest {
    private Long userId;
    private double initialBalance;

    // getters and setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getInitialBalance() {
        return initialBalance;
    }
    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }
}