package com.usarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usarios.dto.UsuarioCrearDTO;
import com.usarios.dto.UsuarioRespuestaDTO;
import com.usarios.hateoas.UsuarioAssembler;
import com.usarios.mapper.UsuarioMapper;
import com.usarios.model.Usuario;
import com.usarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;
import net.datafaker.Faker;
import static org.mockito.Mockito.doThrow;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class) // Carga solo el Controller y sus dependencias, no la aplicación completa.
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // simula llamadas HTTP al controller

    @MockBean //reemplaza beans reales por mocks de mockito
    private UsuarioService service;

    @MockBean
    private UsuarioMapper mapper;

    @MockBean
    private UsuarioAssembler usuarioAssembler;

    // dataFaker para datos ficticios
    private final Faker faker = new Faker();
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Test
    void listarUsuarios_Status200() throws Exception {
        //crear usuario ficticio
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario(faker.name().fullName());
        usuario.setContrasenia(faker.internet().password());

        // el service devuelve el usuario
        when(usuarioService.findAll()).thenReturn(Arrays.asList(usuario));

        // //crear dto de respuesta
        UsuarioRespuestaDTO dto = new UsuarioRespuestaDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        EntityModel<UsuarioRespuestaDTO> entityModel = EntityModel.of(dto);

        // mock del mapper y service
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);
        when(usuarioAssembler.toModel(dto)).thenReturn(entityModel);

        //ejecutar GET y verificar status 200
        // 200 OK: La solicitud ha tenido éxito.
        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk());
    }
    @Test

    void listarUsuarios_SinUsuarios_retorna204() throws Exception {
        // el service devuelve el usuario
        when(usuarioService.findAll()).thenReturn(Arrays.asList());

        // ejecutar get, status 204
        //204 No Content: La solicitud ha tenido éxito, pero no hay contenido que enviar en la respuesta.
        mockMvc.perform(get("/auth"))
                .andExpect(status().isNoContent());
    }


    @Test
    void guardarUsuario() throws Exception {
        //crear dto de entrada con dataFaker
        UsuarioCrearDTO crearDTO = new UsuarioCrearDTO();
        crearDTO.setNombreUsuario(faker.name().fullName());
        crearDTO.setContrasenia(faker.internet().password());

        //crear entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario(faker.name().fullName());
        usuario.setContrasenia(faker.internet().password());

        //crear dto de respuesta
        UsuarioRespuestaDTO respuestaDTO = new UsuarioRespuestaDTO();
        respuestaDTO.setId(usuario.getId());
        respuestaDTO.setNombreUsuario(usuario.getNombreUsuario());

        EntityModel<UsuarioRespuestaDTO> entityModel = EntityModel.of(respuestaDTO);

        //mock del mapper y service
        when(usuarioMapper.toEntity(crearDTO)).thenReturn(usuario);
        when(usuarioService.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(respuestaDTO);
        when(usuarioAssembler.toModel(respuestaDTO)).thenReturn(entityModel);

        // ObjectMapper : Su función es convertir objetos Java a JSON y viceversa.
        ObjectMapper objectMapper = new ObjectMapper();
        // convertir el objeto java crearDTO en un strinng JSON
        String bodyJson = objectMapper.writeValueAsString(crearDTO);

        //ejecutar POST y verificar status 201
        // 201 Created: La solicitud ha sido cumplida y se ha creado un nuevo recurso.
        // mockMvc : es el objeto que te permite simular peticiones HTTP en tus pruebas (sin levantar un servidor real).
        // perform :  ejecuta una petición simulada al endpoint que quieras testear.
        mockMvc.perform(post("/auth")
                        .contentType("application/json")
                        .content(bodyJson))
                .andExpect(status().isCreated());


    }

    @Test
    void buscarUsuarioPorId() throws Exception {
        // datos de entrada
        // los parámetros o valores que se usaran para probar el endpoint.
        Long idBuscado = 1L;

        //crear usuario ficticio
        Usuario usuario = new Usuario();
        usuario.setId(idBuscado);
        usuario.setNombreUsuario(faker.name().fullName());
        usuario.setContrasenia(faker.internet().password());

        // Dto de respuesta
        UsuarioRespuestaDTO dto = new UsuarioRespuestaDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        EntityModel<UsuarioRespuestaDTO> entityModel = EntityModel.of(dto);

        // mock del service, mapper y assembler
        when(usuarioService.findById(idBuscado)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);
        when(usuarioAssembler.toModel(dto)).thenReturn(entityModel);

        //ejecutar GET/auth{id} y verificar status 200
        mockMvc.perform(get("/auth/{id}" , idBuscado))
                .andExpect(status().isOk());

    }
    @Test
    void buscarUsuarioPorId_NoEncontrado_status404() throws Exception {
        Long idNoExiste = 999L;
        when(usuarioService.findById(idNoExiste)).thenThrow(new RuntimeException("Usuario no encontrado"));
        mockMvc.perform(get("/auth/{id}", idNoExiste))
                .andExpect(status().isNotFound());
    }


    @Test
    void actualizarUsuario_RetornaStatus200() throws Exception {
        //datos de entrada
        // idActualizar es el ID del usuario a actualizar.
        Long idActualizar = 1L;

        // crear DTO de entrada (lo que el usuario envia para actualizar)
        // Simula los datos que el cliente enviaría para actualizar.
        UsuarioCrearDTO dtoEntrada = new UsuarioCrearDTO();
        dtoEntrada.setNombreUsuario(faker.name().fullName());
        dtoEntrada.setContrasenia(faker.internet().password());

        // usuario existente simulado
        // Simula el usuario encontrado en base de datos antes de actualizar.
        Usuario existente = new Usuario();
        existente.setId(idActualizar);
        existente.setNombreUsuario("Nombre antiguo");
        existente.setContrasenia("Contrasenia antiguo");

        //usuario actualizado simulacion
        // Representa cómo quedaría después de actualizar los datos.
        Usuario actualizado = new Usuario();
        actualizado.setId(idActualizar);
        actualizado.setNombreUsuario(dtoEntrada.getNombreUsuario());
        actualizado.setContrasenia(dtoEntrada.getContrasenia());

        //DTO de respuesta
        // El objeto que el controller devolvería después de actualizar.
        UsuarioRespuestaDTO respuestaDTO = new UsuarioRespuestaDTO();
        respuestaDTO.setId(actualizado.getId());
        respuestaDTO.setNombreUsuario(actualizado.getNombreUsuario());
        EntityModel<UsuarioRespuestaDTO> entityModel = EntityModel.of(respuestaDTO);

        // mock del service, mapper y assembler
        //retorna el usuario existente.
        when(usuarioService.findById(idActualizar)).thenReturn(existente);

        //guarda y retorna el usuario actualizado.
        when(usuarioService.save(existente)).thenReturn(existente);
        // Mapper y assembler devuelven el DTO y el modelo final.
        when(usuarioMapper.toDTO(actualizado)).thenReturn(respuestaDTO);
        when(usuarioAssembler.toModel(respuestaDTO)).thenReturn(entityModel);

        // ejecutar PUT y verificar status 200
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyJson = objectMapper.writeValueAsString(dtoEntrada);

        // Simula la llamada PUT /auth/{id} con el body en JSON y espera un status 200 OK.
        mockMvc.perform(put("/auth/{id}", idActualizar)
                .contentType("application/json")
                .content(bodyJson))
                .andExpect(status().isOk());



    }

    @Test
    void eliminarUsuario_RetornaStatus204() throws Exception {
        // dato de entrada (id del usuario a eliminar)
        Long idEliminar = 1L;

        //Mock del service: simular que la eliminación no lanza excepción
        doNothing().when(usuarioService).delete(idEliminar);

        // ejecutar DELETE y verificar status 204
        mockMvc.perform(delete("/auth/{id}", idEliminar))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarUsuario_usuarioNoEncontrado_status404() throws Exception {
        Long idNoExiste = 999L;

        // Simula que el service lanza una excepción
        doThrow(new RuntimeException("Usuario no encontrado"))
                .when(usuarioService).delete(idNoExiste);

        mockMvc.perform(delete("/auth/{id}", idNoExiste))
                .andExpect(status().isNotFound());
    }

}