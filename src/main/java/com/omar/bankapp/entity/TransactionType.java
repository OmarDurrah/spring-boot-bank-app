package com.omar.bankapp.entity;

/**
 * Enumeration representing the type of bank transaction.
 *
 * Using ENUM improves readability and prevents invalid values
 * from being stored in the database.
 */
public enum TransactionType {

    /**
     * Money added to the account.
     */
    DEPOSIT,

    /**
     * Money withdrawn from the account.
     */
    WITHDRAW
}