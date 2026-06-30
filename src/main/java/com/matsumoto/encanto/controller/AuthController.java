package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.dto.LoginRequest;
import com.matsumoto.encanto.dto.LoginResponse;
import com.matsumoto.encanto.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        // 1. autenticar com AuthenticationManager
        String email = request.getEmail();
        String senha = request.getSenha();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
        // 2. extrair UserDetails do resultado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 3. gerar token com JwtService
        String token = jwtService.gerarToken(userDetails);
        // 4. retornar LoginResponse com o token
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
