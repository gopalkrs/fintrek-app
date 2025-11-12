package com.gkrs.fintrek.fintrek_app.controllers;

import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionInfoDTO;
import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.user.UserResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.Transactions;
import com.gkrs.fintrek.fintrek_app.services.TransactionService;
import com.gkrs.fintrek.fintrek_app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService,
                                 UserService userService){
        this.transactionService = transactionService;
        this.userService = userService;
    }


    @PostMapping("/account/{accountId}")
    public ResponseEntity<?> createNewTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequest,
                                                  BindingResult bindingResult,
                                                  @PathVariable Long accountId){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(error -> error.getDefaultMessage()).toList());
        }
        try{
            transactionService.createNewTransaction(transactionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("New transaction added.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding new transaction.");
        }
    }

    @DeleteMapping("/account/{accountId}/txn/{transactionId}/delete")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId,
                                                    @PathVariable Long accountId){
        try{
            UserResponseDTO user = userService.getUserProfile();
            if(user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            transactionService.deleteTransaction(user.getId(), accountId, transactionId);
            return ResponseEntity.status(HttpStatus.OK).body("Transaction deleted successfully.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/account/{accountId}/txn/{transactionId}")
    public ResponseEntity<TransactionInfoDTO> getTransactionInfo(
            @PathVariable Long transactionId,
            @PathVariable Long accountId){
        try {
            UserResponseDTO user = userService.getUserProfile();
            if(user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            TransactionInfoDTO transaction = transactionService.getTransactionById(user.getId(), accountId, transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
