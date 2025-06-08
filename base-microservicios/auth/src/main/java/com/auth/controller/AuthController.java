package com.auth.controller;


import com.auth.Dto.LoginRequest;
import com.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequest request) {
        boolean resultado = authService.login(request );

        if ( resultado ) {
            return ResponseEntity.ok("Login correcto");
        }else {
            return ResponseEntity.ok("Login incorrecto");
        }
    }

}
