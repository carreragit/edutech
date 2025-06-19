package com.auth.service;

import com.auth.Dto.LoginRequest;
import com.auth.model.Usuario;
import com.auth.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    private final Faker faker = new Faker();


    @Test
    void loginExitoso_retornaTrue() {
        //datos ficticios
        String username = faker.name().username();
        String password = faker.internet().password();

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(username); // usuario se gurada con username aleatorio
        usuario.setContrasenia(password);  // usuario se guarda con contrasenia aleatorio


        LoginRequest request = new LoginRequest();
        request.setNombreUsuario(username); // request usa el mismo username
        request.setContrasenia(password);  // request usa la misma contrasenia

        // simula que el usuario existe en el repositorio
        when(usuarioRepository.findByNombreUsuario(username)).thenReturn(Optional.of(usuario));

        // ejecuta el metoo y verifica que retorne true
        assertTrue(authService.login(request));
    }

    @Test
    void login_UsuarioNoExiste_retornaFalse() {
        // genera datos ficticios
        String username = faker.name().username();
        String password = faker.internet().password();

        LoginRequest request = new LoginRequest();
        request.setNombreUsuario(username); // request usa el username aleatorio
        request.setContrasenia(password); // request usa la contrasenia aleatoria

        // simula que el usuario NO existe en el repositorio
        when(usuarioRepository.findByNombreUsuario(username))
                .thenReturn(Optional.empty());  // retorna vacio

        // ejecuta el metodo y verifica que retorna false
        assertFalse(authService.login(request));

    }

    @Test
    void login_contrasenaIncorrecta_retornaFalse() {
        // genera datos ficticios , dos contrasenias distintas
        String username = faker.name().username();
        String password = faker.internet().password();
        String otrasPassword = faker.internet().password();

        // crea el usuario de "base de datos" con username y la otra contraseña (NO la del request)
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(username);  // Guarda el mismo username
        usuario.setContrasenia(otrasPassword); // Pero con contraseña distinta

        //crea el objeto LoginRequest que simula la petición del cliente
        LoginRequest request = new LoginRequest();
        request.setNombreUsuario(username); // El usuario intenta loguearse con el mismo username
        request.setContrasenia(password);    // Pero con una contraseña diferente

        // simula el comportamiento del repositorio: cuando buscan el username, encuentran al usuario
        when(usuarioRepository.findByNombreUsuario(username)).thenReturn(Optional.of(usuario));

        // llama al metodo login del AuthService y verifica que retorna false (contraseña incorrecta)
        assertFalse(authService.login(request));
    }

}