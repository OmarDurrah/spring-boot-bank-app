package com.omar.bankapp.service;

import com.omar.bankapp.entity.BankAccount;
import com.omar.bankapp.entity.User;
import com.omar.bankapp.repository.BankAccountRepository;
import com.omar.bankapp.repository.UserRepository;
import com.omar.bankapp.exception.AccountNotFoundException;
import com.omar.bankapp.exception.InsufficientBalanceException;
import com.omar.bankapp.entity.Transaction;
import com.omar.bankapp.entity.TransactionType;
import com.omar.bankapp.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.omar.bankapp.dto.*;

/**
 * Service layer responsible for handling all business logic
 * of the banking system.
 *
 * Responsibilities:
 * - Managing users
 * - Creating bank accounts
 * - Depositing and withdrawing money
 * - Transferring funds between accounts
 * - Recording transactions
 *
 * @Service marks this class as a Spring service component.
 * @Transactional ensures that all operations inside this service
 * are executed within a database transaction.
 */
@Service
@Transactional
public class BankService {

    private final UserRepository userRepository;
    private final BankAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection.
     */
    public BankService(UserRepository userRepository,
            BankAccountRepository accountRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user.
     *
     * The password is encrypted using BCrypt before storing it
     * in the database to ensure security.
     *
     * @param name username
     * @param password raw password
     * @return saved User entity
     */
    public User createUser(String name, String password) {

        User user = new User();

        user.setName(name);

        // Hash password before storing
        String hashedPassword = passwordEncoder.encode(password);

        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    /**
     * Create a new bank account for an existing user.
     *
     * @param userId ID of the account owner
     * @param initialBalance starting balance
     * @return created BankAccount
     */
    public BankAccount createAccount(Long userId, double initialBalance) {

        // Retrieve user from database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create new account
        BankAccount account = new BankAccount(initialBalance);

        // Assign account owner
        account.setUser(user);

        return accountRepository.save(account);
    }

    /**
     * Deposit money into an account.
     *
     * Steps:
     * 1. Retrieve account
     * 2. Add money to balance
     * 3. Record transaction
     *
     * @param accountId target account
     * @param amount deposit amount
     * @return updated account response
     */
    public AccountResponse deposit(Long accountId, double amount) {

        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Update account balance
        account.deposit(amount);

        // Record transaction
        Transaction transaction =
                new Transaction(account, TransactionType.DEPOSIT, amount);

        transactionRepository.save(transaction);

        return new AccountResponse(account.getId(), account.getBalance());
    }

    /**
     * Withdraw money from an account.
     *
     * Validations:
     * - Account must exist
     * - Amount must be positive
     * - Account must have enough balance
     *
     * @param accountId target account
     * @param amount withdrawal amount
     * @return updated account information
     */
    public AccountResponse withdraw(Long accountId, double amount) {

        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (amount > account.getBalance()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Deduct balance
        account.withdraw(amount);

        // Record transaction
        Transaction transaction =
                new Transaction(account, TransactionType.WITHDRAW, amount);

        transactionRepository.save(transaction);

        return new AccountResponse(account.getId(), account.getBalance());
    }

    /**
     * Retrieve transaction history for an account with pagination.
     *
     * Pagination prevents loading large datasets into memory.
     *
     * @param accountId target account
     * @param pageable pagination configuration
     * @return paginated transaction list
     */
    public Page<TransactionResponse> getAccountTransactions(Long accountId, Pageable pageable) {

        Page<Transaction> transactions =
                transactionRepository.findByAccountId(accountId, pageable);

        // Convert Entity -> DTO
        return transactions.map(t -> new TransactionResponse(
                t.getId(),
                t.getAccount().getId(),
                t.getType(),
                t.getAmount(),
                t.getCreatedAt()));
    }

    /**
     * Transfer money between two accounts.
     *
     * Steps:
     * 1. Validate accounts
     * 2. Ensure amount is valid
     * 3. Withdraw from source
     * 4. Deposit into destination
     * 5. Record two transactions
     *
     * Because the service is transactional, if any step fails,
     * the entire operation will rollback automatically.
     *
     * @param fromAccountId sender account
     * @param toAccountId receiver account
     * @param amount transfer amount
     * @return transfer result
     */
    public TransferResponse transfer(Long fromAccountId, Long toAccountId, double amount) {

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        BankAccount from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        BankAccount to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (from.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Perform transfer
        from.withdraw(amount);
        to.deposit(amount);

        // Record withdrawal transaction
        transactionRepository.save(
                new Transaction(from, TransactionType.WITHDRAW, amount));

        // Record deposit transaction
        transactionRepository.save(
                new Transaction(to, TransactionType.DEPOSIT, amount));

        return new TransferResponse(fromAccountId, toAccountId, amount);
    }

    /**
     * Basic login validation.
     *
     * Checks whether a user exists and verifies the password.
     */
    public void login(String name, String password) {

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }
}