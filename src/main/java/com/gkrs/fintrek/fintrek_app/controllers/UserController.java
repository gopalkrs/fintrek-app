package com.gkrs.fintrek.fintrek_app.controllers;

import com.gkrs.fintrek.fintrek_app.dto.user.UserRequestDTO;
import com.gkrs.fintrek.fintrek_app.dto.user.UserResponseDTO;
import com.gkrs.fintrek.fintrek_app.entity.User;
import com.gkrs.fintrek.fintrek_app.mapper.UserMapper;
import com.gkrs.fintrek.fintrek_app.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userData,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(error -> error.getDefaultMessage()).toList());
        }

        try{
            Optional<User> userExists = userService.getUserByEmail(userData.getEmail());
            if(userExists.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email already exists, please use another email.");
            }
            UserResponseDTO newUser = userService.createUser(userData);
            if(newUser != null){
                logger.info("created user: {}", newUser.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body("User created : {}");
            } else {
                logger.warn("Failed to create user : {}", userData);
                return ResponseEntity.status( HttpStatus.BAD_REQUEST).body("Failed to create user");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long id){
        Optional<User> userExists = userService.getUserInfo(id);
        if(userExists.isPresent()){
            logger.info("Fetched user info for user id: {}", id);
            return new ResponseEntity<>(userMapper.toDTO((userExists.get())), HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

}
