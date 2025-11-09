package com.gkrs.fintrek.fintrek_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Category> category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limitAmount;

    private Date periodStartDate;
    private Date periodEndDate;

}
