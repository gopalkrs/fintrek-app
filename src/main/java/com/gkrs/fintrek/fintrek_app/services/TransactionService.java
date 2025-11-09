package com.gkrs.fintrek.fintrek_app.services;

import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionRequestDTO;
import com.gkrs.fintrek.fintrek_app.entity.Category;
import com.gkrs.fintrek.fintrek_app.entity.Transactions;
import com.gkrs.fintrek.fintrek_app.entity.User;
import com.gkrs.fintrek.fintrek_app.exception.ResourceNotFoundException;
import com.gkrs.fintrek.fintrek_app.repositories.AccountRepository;
import com.gkrs.fintrek.fintrek_app.repositories.CategoryRepository;
import com.gkrs.fintrek.fintrek_app.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
    }
    @Transactional
    public void createNewTransaction(TransactionRequestDTO transactionRequest){

        var account = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        User user = account.getUser();
        var category = categoryRepository.findByCategoryNameAndUser(transactionRequest.getCategoryName(), user)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(transactionRequest.getCategoryName().toLowerCase());
                    newCategory.setTransactionType(transactionRequest.getTransactionType());
                    newCategory.setUser(user);
                    return categoryRepository.save(newCategory);
                });

        Transactions txn = new Transactions();
        txn.setAmount(transactionRequest.getAmount());
        txn.setTransactionType(transactionRequest.getTransactionType());
        txn.setCategory(category);
        txn.setAccount(account);
        txn.setDescription(transactionRequest.getDescription());
        txn.setTransactionDate(transactionRequest.getTransactionDate());
        transactionRepository.save(txn);
    }
}
