package com.gkrs.fintrek.fintrek_app.controllers;

import com.gkrs.fintrek.fintrek_app.dto.AuthResponse;
import com.gkrs.fintrek.fintrek_app.dto.LoginRequest;
import com.gkrs.fintrek.fintrek_app.services.JWTUtilService;
import com.gkrs.fintrek.fintrek_app.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTUtilService jwtUtil;

    public AuthController(AuthService authService, JWTUtilService jwtUtil){
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginData) {
        if(authService.authenticate(loginData.getEmail(), loginData.getPassword())){
            String token = jwtUtil.generateToken(loginData.getEmail());
            return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
}
