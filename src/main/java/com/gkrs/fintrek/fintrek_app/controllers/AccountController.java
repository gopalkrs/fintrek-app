package com.gkrs.fintrek.fintrek_app.controllers;

import com.gkrs.fintrek.fintrek_app.dto.account.AccountRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.account.AccountResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.Account;
import com.gkrs.fintrek.fintrek_app.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO,
                                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(error -> error.getDefaultMessage()).toList());
        }
        try{
            accountService.addNewAccount(accountRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Account added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountInfo(@PathVariable Long id){
        AccountResponseDTO account = accountService.getAccountInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByUserId(@PathVariable Long userId){
        List<AccountResponseDTO> accountsList = accountService.getAccountsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsList);
    }

    @DeleteMapping("/delete/user/{userId}/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId, @PathVariable Long accountId){
        try{
            accountService.deleteAccount(accountId, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
