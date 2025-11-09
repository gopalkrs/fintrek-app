package com.gkrs.fintrek.fintrek_app.dto.account;

import com.gkrs.fintrek.fintrek_app.entity.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;
    private Long userId;
    private LocalDateTime createdAt;
}

