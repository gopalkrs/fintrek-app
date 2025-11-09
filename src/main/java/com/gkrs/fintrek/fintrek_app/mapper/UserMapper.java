package com.gkrs.fintrek.fintrek_app.mapper;

import com.gkrs.fintrek.fintrek_app.dto.user.UserRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.user.UserResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public UserResponseDTO toDTO(User user){
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public User toEntity(UserResponseDTO dto){
        return modelMapper.map(dto, User.class);
    }

    public User toEntityUserRequest(UserRequestDTO dto){
        return modelMapper.map(dto, User.class);
    }

    public UserRequestDTO toDTOUserRequest(User user){
        return modelMapper.map(user, UserRequestDTO.class);
    }


}
