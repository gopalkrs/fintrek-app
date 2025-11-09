package com.gkrs.fintrek.fintrek_app.services;

import com.gkrs.fintrek.fintrek_app.dto.account.AccountRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.account.AccountResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.Account;
import com.gkrs.fintrek.fintrek_app.exception.ResourceNotFoundException;
import com.gkrs.fintrek.fintrek_app.mapper.AccountMapper;
import com.gkrs.fintrek.fintrek_app.repositories.AccountRepository;
import com.gkrs.fintrek.fintrek_app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper,
                          UserRepository userRepository){
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addNewAccount(AccountRequestDTO accountRequestDTO){
        try{
            var user = userRepository.findById(accountRequestDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Account account = accountMapper.toEntity(accountRequestDTO);
            account.setId(null);
            account.setUser(user);
            user.getAccount().add(account);
            log.info("account data : {}", account);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AccountResponseDTO getAccountInfo(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        return accountMapper.toResponseDTO(account);

    }

    public List<AccountResponseDTO> getAccountsByUserId(Long userId){
        List<Account> accounts = accountRepository.findByUserId(userId);
        if(accounts.isEmpty()){
            throw new ResourceNotFoundException("No accounts found for user id : " + userId);
        }
        return accounts.stream()
                .map(account -> accountMapper.toResponseDTO(account))
                .collect(Collectors.toList());
    }
}
