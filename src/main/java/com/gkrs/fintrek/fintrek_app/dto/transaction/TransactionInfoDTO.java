package com.gkrs.fintrek.fintrek_app.dto.transaction;

import com.gkrs.fintrek.fintrek_app.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
//t.id, t.transactionType, t.amount, t.transactionDate,t.description, t.currencyType, t.createdAt, u.id, a.id
@Data
@NoArgsConstructor
public class TransactionInfoDTO {
    private Long transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Date transactionDate;
    private String description;
    private String currencyType;
    private LocalDateTime createdAt;
    private Long userId;
    private Long accountId;

    public TransactionInfoDTO(Long transactionId, TransactionType transactionType, BigDecimal amount,
                              Date transactionDate, String description, String currencyType,
                              LocalDateTime createdAt, Long userId, Long accountId) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
        this.currencyType = currencyType;
        this.createdAt = createdAt;
        this.userId = userId;
        this.accountId = accountId;
    }


    // Getters and setters (or use Lombok @Data)
}
