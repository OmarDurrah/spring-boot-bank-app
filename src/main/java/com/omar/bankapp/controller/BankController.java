package com.omar.bankapp.controller;

import com.omar.bankapp.dto.*;
import com.omar.bankapp.entity.*;
import com.omar.bankapp.service.BankService;

import jakarta.validation.Valid;
import com.omar.bankapp.security.*;
import com.omar.bankapp.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.omar.bankapp.exception.*;

/**
 * Main REST controller for the Banking Application.
 *
 * This controller exposes API endpoints that allow clients to:
 * - Register users
 * - Authenticate (login)
 * - Create bank accounts
 * - Deposit money
 * - Withdraw money
 * - Transfer money
 * - View transaction history
 *
 * All endpoints are prefixed with "/api".
 */
@RestController
@RequestMapping("/api")
public class BankController {

    /**
     * Service layer containing the business logic of the banking system.
     */
    private final BankService bankService;

    /**
     * Repository used for retrieving users during authentication.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder used to verify hashed passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service responsible for generating JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * Constructor-based dependency injection.
     */
    public BankController(BankService bankService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.bankService = bankService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    /**
     * Register a new user in the system.
     *
     * Endpoint:
     * POST /api/auth/register
     *
     * This endpoint is public (permitted in SecurityConfig).
     *
     * @param request user registration data
     * @return created User
     */
    @PostMapping("/auth/register")
    public User register(@Valid @RequestBody CreateUserRequest request) {
        return bankService.createUser(request.getName(), request.getPassword());
    }


    /**
     * Create a new bank account for an existing user.
     *
     * Endpoint:
     * POST /api/accounts
     *
     * @param request contains userId and initial balance
     * @return created BankAccount
     */
    @PostMapping("/accounts")
    public BankAccount createAccount(@RequestBody AccountRequest request) {

        return bankService.createAccount(
                request.getUserId(),
                request.getInitialBalance());
    }


    /**
     * Deposit money into a bank account.
     *
     * Endpoint:
     * POST /api/deposit
     *
     * @param request deposit request containing accountId and amount
     * @return updated account information
     */
    @PostMapping("/deposit")
    public AccountResponse deposit(@Valid @RequestBody DepositRequest request) {

        return bankService.deposit(
                request.getAccountId(),
                request.getAmount());
    }


    /**
     * Withdraw money from a bank account.
     *
     * Endpoint:
     * POST /api/withdraw
     *
     * @param request withdrawal request containing accountId and amount
     * @return updated account information
     */
    @PostMapping("/withdraw")
    public AccountResponse withdraw(@Valid @RequestBody WithdrawRequest request) {

        return bankService.withdraw(
                request.getAccountId(),
                request.getAmount());
    }


    /**
     * Retrieve transaction history for a specific account.
     *
     * Endpoint:
     * GET /api/transactions
     *
     * Pagination is supported to avoid loading too many
     * transactions at once.
     *
     * Example:
     * /api/transactions?accountId=1&page=0&size=5
     *
     * @param accountId target account
     * @param pageable pagination configuration
     * @return paginated list of transactions
     */
    @GetMapping("/transactions")
    public Page<TransactionResponse> getTransactions(
            @RequestParam Long accountId,
            @PageableDefault(size = 5) Pageable pageable) {

        return bankService.getAccountTransactions(accountId, pageable);
    }


    /**
     * Authenticate user and generate JWT token.
     *
     * Endpoint:
     * POST /api/auth/login
     *
     * Steps:
     * 1. Retrieve user from database
     * 2. Verify password using BCrypt
     * 3. Generate JWT token
     * 4. Return token to client
     *
     * @param request login credentials
     * @return JWT token
     */
    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        // Retrieve user from database
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new AccountNotFoundException("Invalid credentials"));

        // Verify password using BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AccountNotFoundException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getName());

        // Debug print (remove in production)
        System.out.println(token);

        return new LoginResponse(token);
    }


    /**
     * Transfer money between two bank accounts.
     *
     * Endpoint:
     * POST /api/transfer
     *
     * @param request transfer request containing:
     *                - source account
     *                - destination account
     *                - amount
     *
     * @return transfer result
     */
    @PostMapping("/transfer")
    public TransferResponse transfer(@Valid @RequestBody TransferRequest request) {

        return bankService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
    }

}
