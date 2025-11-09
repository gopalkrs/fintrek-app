package com.gkrs.fintrek.fintrek_app.entity;

import com.gkrs.fintrek.fintrek_app.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.STRING)    //  credit/debit
    @Column(nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(nullable=false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 5, nullable = false)
    private final String currencyType = "INR";

    private String description;

//    @Column(nullable = false, length = 20)
//    private String categoryName;

    private Date transactionDate;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
