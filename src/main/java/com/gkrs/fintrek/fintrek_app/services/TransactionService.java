package com.gkrs.fintrek.fintrek_app.services;

import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionInfoDTO;
import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionRequestDTO;
import com.gkrs.fintrek.fintrek_app.entity.Account;
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
import java.util.List;
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
        txn.setUser(managedUser);
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

    @Transactional
    public void deleteTransaction(Long userId, Long accountId, Long transactionId){
        Account account = accountRepository.findByIdWithUser(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        TransactionInfoDTO txn = getTransactionById(userId, accountId, transactionId);
        if(txn.getTransactionType().toString().equals("CREDIT")){
            account.setBalance(account.getBalance().add(txn.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(txn.getAmount()));
        }
        accountRepository.save(account);
        transactionRepository.deleteById(txn.getTransactionId());
    }

    public TransactionInfoDTO getTransactionById(Long userId, Long accountId, Long transactionId){

        Optional<TransactionInfoDTO> txn = transactionRepository.getTransactionInfo(userId, accountId, transactionId);
        if(txn.isEmpty()){
            throw new ResourceNotFoundException("Transaction not found with id: " + transactionId);
        } else {
            return txn.get();
        }
    }

    public List<Transactions> getTransactionsByUser(Long userId, Long accountId){
        User user = userService.getUserInfo(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + userId));
        Account account = accountRepository.findByIdWithUser(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + accountId));

        return transactionRepository.findByUserIdAndAccountId(userId, accountId);
    }
}
