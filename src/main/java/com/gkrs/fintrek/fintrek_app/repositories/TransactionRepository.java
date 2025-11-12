package com.gkrs.fintrek.fintrek_app.repositories;

import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionInfoDTO;
import com.gkrs.fintrek.fintrek_app.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    void deleteById(Long transactionId);

    List<Transactions> findByUserIdAndAccountId(Long userId, Long accountId);

    //@Query("SELECT t.id, t.transactionType, t.amount, t.transactionDate,t.description, t.currencyType, t.createdAt, u.id, a.id FROM Transactions t JOIN FETCH t.account a JOIN FETCH a.user u WHERE u.id = :userId AND a.id = :accountId AND t.id = :transactionId")
    @Query("""
    SELECT new com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionInfoDTO(
        t.id,
        t.transactionType,
        t.amount,
        t.transactionDate,
        t.description,
        t.currencyType,
        t.createdAt,
        u.id,
        a.id
        )
        
    FROM Transactions t
    JOIN t.account a
    JOIN a.user u
    WHERE u.id = :userId
      AND a.id = :accountId
      AND t.id = :transactionId
    """)
    Optional<TransactionInfoDTO> getTransactionInfo(@Param("userId") Long userId,
                                                    @Param("accountId") Long accountId,
                                                    @Param("transactionId") Long transactionId);

}
