package com.gkrs.fintrek.fintrek_app.services;

import com.gkrs.fintrek.fintrek_app.dto.user.UserRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.user.UserResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.User;
import com.gkrs.fintrek.fintrek_app.mapper.UserMapper;
import com.gkrs.fintrek.fintrek_app.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

//    public String getHealthStatus(){
//        return "User Service is up and running!";
//    }

    public UserResponseDTO createUser(UserRequestDTO userData){
        try{
            User user = userMapper.toEntityUserRequest(userData);
            user.setPassword(passwordEncoder.encode(userData.getPassword()));
            User newUser = userRepository.save(user);
            return userMapper.toDTO(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> getUserInfo(Long userId){
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public UserResponseDTO getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String
        && authentication.getPrincipal().equals("anonymousUser"))){
            Object principal = authentication.getPrincipal();
            if(principal instanceof User userDetails){
                UserResponseDTO response = new UserResponseDTO();
                response.setEmail(userDetails.getEmail());
                response.setId(userDetails.getId());
                // optionally set other fields if stored in your user entity
                return response;
            }
        }
        return null;
    }

}



