package com.gkrs.fintrek.fintrek_app.repositories;

import com.gkrs.fintrek.fintrek_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a JOIN FETCH a.user u WHERE u.id = :userId")
    List<Account> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Account a JOIN FETCH a.user WHERE a.id = :id")
    Optional<Account> findByIdWithUser(@Param("id") Long id);
}
