package com.gkrs.fintrek.fintrek_app.repositories;

import com.gkrs.fintrek.fintrek_app.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {

}
