package com.gkrs.fintrek.fintrek_app.repositories;

import com.gkrs.fintrek.fintrek_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
