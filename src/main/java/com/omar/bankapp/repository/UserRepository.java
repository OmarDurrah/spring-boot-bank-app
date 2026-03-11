package com.omar.bankapp.repository;

import com.omar.bankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 *
 * Extends JpaRepository which provides built-in CRUD operations:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 *
 * Spring Data JPA automatically generates the implementation
 * at runtime, so no manual implementation is required.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their username.
     *
     * Spring Data JPA automatically generates the query based on
     * the method name.
     *
     * Example generated query:
     * SELECT u FROM User u WHERE u.name = :name
     *
     * @param name the username
     * @return Optional containing the user if found
     */
    Optional<User> findByName(String name);

}