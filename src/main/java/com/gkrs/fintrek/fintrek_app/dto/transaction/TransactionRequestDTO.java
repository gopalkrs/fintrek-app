package com.gkrs.fintrek.fintrek_app.dto.transaction;

import com.gkrs.fintrek.fintrek_app.entity.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class TransactionRequestDTO {
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Category name cannot be null")
    private String categoryName;

    @NotNull(message = "Account id is required")
    private Long accountId;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Date is required")
    private Date transactionDate;


}
