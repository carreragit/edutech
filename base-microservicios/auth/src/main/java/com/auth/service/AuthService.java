package com.auth.service;


import com.auth.Dto.LoginRequest;
import com.auth.model.Usuario;
import com.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired // Inyecta automáticamente una instancia de UsuarioRepository para poder acceder a la base de datos.
    private UsuarioRepository usuarioRepository;

    public boolean login(LoginRequest request) {
        Optional<Usuario> usuarioBuscado = usuarioRepository.findByNombreUsuario(request.getNombreUsuario());

         if (usuarioBuscado.isPresent()) {
             Usuario usuario = usuarioBuscado.get();
             return usuario.getContrasenia().equals(request.getContrasenia());
         }
         return false;
    }
}

