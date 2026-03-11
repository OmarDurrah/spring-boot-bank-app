package com.omar.bankapp.repository;

import com.omar.bankapp.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for BankAccount entities.
 *
 * Handles all database operations related to bank accounts.
 *
 * By extending JpaRepository, this interface automatically
 * inherits methods such as:
 *
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 * - existsById()
 *
 * Spring generates the implementation automatically at runtime.
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}