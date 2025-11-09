package com.gkrs.fintrek.fintrek_app.controllers;

import com.gkrs.fintrek.fintrek_app.dto.transaction.TransactionRequestDTO;
import com.gkrs.fintrek.fintrek_app.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> createNewTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequest,
                                                  BindingResult bindingResult){
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
}
