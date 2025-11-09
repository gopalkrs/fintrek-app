package com.gkrs.fintrek.fintrek_app.mapper;

import com.gkrs.fintrek.fintrek_app.dto.account.AccountRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.account.AccountResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public AccountRequestDTO toDTO(Account account){
        return modelMapper.map(account, AccountRequestDTO.class);
    }

    public Account toEntity(AccountRequestDTO dto){
        return modelMapper.map(dto, Account.class);
    }

    public AccountResponseDTO toResponseDTO(Account account){
        return modelMapper.map(account, AccountResponseDTO.class);
    }

}
