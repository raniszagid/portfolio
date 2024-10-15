package com.example.carservice.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
