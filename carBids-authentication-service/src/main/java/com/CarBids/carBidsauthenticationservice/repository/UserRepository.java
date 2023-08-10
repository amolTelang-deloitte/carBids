package com.CarBids.carBidsauthenticationservice.repository;

import com.CarBids.carBidsauthenticationservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByusername(String username);
}
