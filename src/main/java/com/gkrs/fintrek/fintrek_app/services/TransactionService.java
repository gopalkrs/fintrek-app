package com.gkrs.fintrek.fintrek_app.services;

import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionRequestDTO;
import com.gkrs.fintrek.fintrek_app.entity.Category;
import com.gkrs.fintrek.fintrek_app.entity.Transactions;
import com.gkrs.fintrek.fintrek_app.entity.User;
import com.gkrs.fintrek.fintrek_app.exception.ResourceNotFoundException;
import com.gkrs.fintrek.fintrek_app.repositories.AccountRepository;
import com.gkrs.fintrek.fintrek_app.repositories.CategoryRepository;
import com.gkrs.fintrek.fintrek_app.repositories.TransactionRepository;
import com.gkrs.fintrek.fintrek_app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              AccountRepository accountRepository,
                              UserService userService){
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.userService = userService;
    }
    @Transactional
    public void createNewTransaction(TransactionRequestDTO transactionRequest){


        var account = accountRepository.findByIdWithUser(transactionRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        User user = account.getUser();
        if(user == null){
            // If user is null here, something's wrong in DB: either user_id is null or references missing user
            throw new ResourceNotFoundException("Account has no associated user. user_id may be invalid.");
        }

        User managedUser = userService.getUserInfo(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this account"));

        var category = categoryRepository.findByCategoryNameAndUser(transactionRequest.getCategoryName(), managedUser)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setId(null);
                    newCategory.setCategoryName(transactionRequest.getCategoryName().toLowerCase());
                    newCategory.setTransactionType(transactionRequest.getTransactionType());
                    newCategory.setUser(managedUser);

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

        BigDecimal updatedBalance = account.getBalance();
        if(transactionRequest.getTransactionType().toString().equals("CREDIT")){
            updatedBalance = updatedBalance.add(transactionRequest.getAmount());
        } else {
            updatedBalance = updatedBalance.subtract(transactionRequest.getAmount());
        }
        account.setBalance(updatedBalance);
        accountRepository.save(account);
    }
}
