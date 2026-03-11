package com.omar.bankapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import com.omar.bankapp.dto.*;

/**
 * Global exception handler for the entire application.
 *
 * This class centralizes the handling of exceptions thrown by
 * controllers and services. Instead of returning raw stack traces,
 * it returns a structured ErrorResponse object with a proper HTTP status.
 *
 * @RestControllerAdvice allows Spring to intercept exceptions
 * thrown from any @RestController in the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AccountNotFoundException.
     *
     * This exception is thrown when the requested account
     * does not exist in the database.
     *
     * @param ex the thrown exception
     * @return ResponseEntity containing an ErrorResponse with HTTP 404 status
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {

        // Create standardized error response object
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());

        // Print error message in server logs (use logger in production)
        System.out.println(error.getMessage());

        // Return HTTP 404 response to the client
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InsufficientBalanceException.
     *
     * This exception is thrown when a user attempts to withdraw
     * or transfer an amount greater than the available balance.
     *
     * @param ex the thrown exception
     * @return ResponseEntity containing an ErrorResponse with HTTP 400 status
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {

        // Create error response with HTTP 400 (Bad Request)
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles optimistic locking conflicts.
     *
     * This occurs when two transactions try to update the same
     * database record simultaneously. Hibernate throws this exception
     * to prevent lost updates.
     *
     * Example:
     * Two users try to transfer money from the same account at the same time.
     *
     * @return ResponseEntity containing an ErrorResponse with HTTP 409 status
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock() {

        // Return conflict response indicating concurrent modification
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Account was modified. Please retry."
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles any unhandled or unexpected exceptions.
     *
     * This acts as a fallback handler to prevent exposing
     * internal errors or stack traces to the client.
     *
     * @param ex the unexpected exception
     * @return ResponseEntity containing an ErrorResponse with HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        // Generic error response for unexpected server errors
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected server error"
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}