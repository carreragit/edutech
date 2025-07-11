package com.auth.controller;

import com.auth.Dto.LoginRequest;
import com.auth.Dto.LoginResponseDTO;
import com.auth.hateoas.LoginResponseAssembler;
import com.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "API para manejar la autenticación de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LoginResponseAssembler assembler;

    @Operation(summary = "Iniciar sesión", description = "Autentica las credenciales del usuario y devuelve un resultado")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody LoginRequest request) {
        boolean resultado = authService.login(request);
        LoginResponseDTO responseDto = assembler.toModel(resultado);
        return ResponseEntity.ok(responseDto);
    }

}
