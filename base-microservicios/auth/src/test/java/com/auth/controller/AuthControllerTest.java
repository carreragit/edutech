package com.auth.controller;

import com.auth.Dto.LoginResponseDTO;
import com.auth.hateoas.LoginResponseAssembler;
import com.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import net.datafaker.Faker;
import com.auth.Dto.LoginRequest;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class) //solo carga el controller y sus dependencias, no toda la app.
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc; // permite simular llamadas HTTP

    @MockBean //crea mocks de los beans usados por el controller
    private AuthService authService; //simula el servicio de autenticación

    @MockBean //crea mocks de los beans usados por el controller
    private LoginResponseAssembler assembler; // simula el assembler que arma la respuesta

    private final ObjectMapper objectMapper = new ObjectMapper(); //convierte objetos a JSON

    private final Faker faker = new Faker(); //genera datos ficticios aleatorios

    @Test
    void loginExitoso() throws Exception {
        //generar datos aleatorios
        String usuario = faker.name().firstName();
        String password = faker.name().firstName();

        LoginRequest request = new LoginRequest();
        request.setNombreUsuario(usuario);
        request.setContrasenia(password);

        // simula que el login es exitoso (devuelve true).
        boolean resultadoLogin = true;
        String mensaje = "Login exitoso";
        LoginResponseDTO resultadoDto = new LoginResponseDTO(resultadoLogin, mensaje);

        when(authService.login(any(LoginRequest.class))).thenReturn(resultadoLogin);
        //arma el DTO de respuesta con el resultado y el mensaje esperado.
        when(assembler.toModel(resultadoLogin)).thenReturn(resultadoDto);

        // convierte el loginRequest a un string JSON
        String bodyJson = objectMapper.writeValueAsString(request);

        // ejecutamos POST
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                //verifica que el controller responde HTTP 200.
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.message").value(mensaje));

    }
    @Test
    void loginFallido() throws Exception {
        //generar datos aleatorios
        String usuario = faker.name().firstName();
        String password = faker.name().firstName();

        LoginRequest request = new LoginRequest();
        request.setNombreUsuario(usuario);
        request.setContrasenia(password);

        // simula que el login es exitoso (devuelve true).
        boolean resultadoLogin = false;
        String mensaje = "Login fallido";
        LoginResponseDTO resultadoDto = new LoginResponseDTO(resultadoLogin, mensaje);

        when(authService.login(any(LoginRequest.class))).thenReturn(resultadoLogin);
        //arma el DTO de respuesta con el resultado y el mensaje esperado.
        when(assembler.toModel(resultadoLogin)).thenReturn(resultadoDto);

        // convierte el loginRequest a un string JSON
        String bodyJson = objectMapper.writeValueAsString(request);

        // ejecutamos POST
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson))
                //verifica que el controller responde HTTP 200.
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.message").value(mensaje));

    }


}