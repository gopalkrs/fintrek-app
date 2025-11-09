package com.gkrs.fintrek.fintrek_app.entity;

import com.gkrs.fintrek.fintrek_app.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String categoryName;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> transactions;

//    @ManyToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Budget> budget;

}
