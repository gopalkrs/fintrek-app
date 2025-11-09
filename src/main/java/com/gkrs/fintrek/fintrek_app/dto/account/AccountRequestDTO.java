package com.gkrs.fintrek.fintrek_app.dto.account;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    @NotBlank(message = "Account name cannot be blank")
    @Size(min = 2, max = 50, message = "Account name must be at least 2-50 characters")
    private String accountName;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0" , message = "Balance cannot be negative")
    private BigDecimal balance;

    @NotNull(message = "User Id is required")
    private Long userId;
}
