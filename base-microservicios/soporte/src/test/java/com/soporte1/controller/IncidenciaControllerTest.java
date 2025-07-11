package com.soporte1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soporte1.dto.IncidenciaCrearDTO;
import com.soporte1.dto.IncidenciaRespuestaDTO;
import com.soporte1.hateoas.IncidenciaModelAssembler;
import com.soporte1.mapper.IncidenciaMapper;
import com.soporte1.model.Incidencia;
import com.soporte1.service.IncidenciaService;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidenciaController.class)
class IncidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular llamadas HTTP al controller

    @MockBean
    private IncidenciaService incidenciaService;

    @MockBean
    private IncidenciaMapper mapper;

    @MockBean
    private IncidenciaModelAssembler assembler;

    private final Faker faker = new Faker(); // Para generar datos ficticios

    @Test
    void listarIncidencias_Status200() throws Exception {
        // Paso 1: Crear una incidencia ficticia con datos generados
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1L);
        incidencia.setTitulo(faker.lorem().sentence());
        incidencia.setDescripcion(faker.lorem().paragraph());

        // Paso 2: Crear DTO que representa la respuesta esperada
        IncidenciaRespuestaDTO dto = new IncidenciaRespuestaDTO();
        dto.setId(incidencia.getId());
        dto.setTitulo(incidencia.getTitulo());
        dto.setDescripcion(incidencia.getDescripcion());

        // Paso 3: Crear EntityModel con enlace self (HATEOAS)
        EntityModel<IncidenciaRespuestaDTO> recurso = EntityModel.of(
                dto,
                linkTo(methodOn(IncidenciaController.class).buscar(dto.getId())).withSelfRel()
        );

        // Paso 4: Mock en el servicio para devolver la lista con una incidencia
        when(incidenciaService.findAll()).thenReturn(Arrays.asList(incidencia));
        when(mapper.toDTO(incidencia)).thenReturn(dto);
        when(assembler.toModel(dto)).thenReturn(recurso);

        // Paso 5: Ejecutar la peticion GET /api/incidencias y esperar status 200 OK
        mockMvc.perform(get("/api/incidencias"))
                .andExpect(status().isOk());
    }

    @Test
    void listarIncidencias_SinIncidencias_retorna204() throws Exception {
        // Paso 1: Mockear servicio para devolver lista vacía
        when(incidenciaService.findAll()).thenReturn(Collections.emptyList());

        // Paso 2: Ejecutar GET y esperar status 204 No Content
        mockMvc.perform(get("/api/incidencias"))
                .andExpect(status().isNoContent());

        // Paso 3: Verificar que el servicio fue llamado y que no se interactuó con mapper ni assembler
        verify(incidenciaService).findAll();
        verifyNoInteractions(mapper, assembler);
    }

    @Test
    void guardarIncidencia_Status201() throws Exception {
        IncidenciaCrearDTO crearDTO = new IncidenciaCrearDTO();
        crearDTO.setTitulo(faker.lorem().sentence());
        crearDTO.setDescripcion(faker.lorem().paragraph());

        // Paso 2: Crear entidad Incidencia simulando que se va a guardar (sin id aun)
        Incidencia incidencia = new Incidencia();
        incidencia.setTitulo(crearDTO.getTitulo());
        incidencia.setDescripcion(crearDTO.getDescripcion());

        Incidencia incidenciaGuardada = new Incidencia();
        incidenciaGuardada.setId(1L);
        incidenciaGuardada.setTitulo(crearDTO.getTitulo());
        incidenciaGuardada.setDescripcion(crearDTO.getDescripcion());

        IncidenciaRespuestaDTO respuestaDTO = new IncidenciaRespuestaDTO();
        respuestaDTO.setId(incidenciaGuardada.getId());
        respuestaDTO.setTitulo(incidenciaGuardada.getTitulo());
        respuestaDTO.setDescripcion(incidenciaGuardada.getDescripcion());

        EntityModel<IncidenciaRespuestaDTO> recurso = EntityModel.of(respuestaDTO);

        when(mapper.toEntity(crearDTO)).thenReturn(incidencia);
        when(incidenciaService.save(incidencia)).thenReturn(incidenciaGuardada);
        when(mapper.toDTO(incidenciaGuardada)).thenReturn(respuestaDTO);
        when(assembler.toModel(respuestaDTO)).thenReturn(recurso);

        // Paso 7: Serializar el DTO crear a JSON para enviar en la peticion
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(crearDTO);

        // Paso 8: Ejecutar POST /api/incidencias con contenido JSON y esperar status 201 Created
        mockMvc.perform(post("/api/incidencias")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(mapper).toEntity(crearDTO);
        verify(incidenciaService).save(incidencia);
        verify(mapper).toDTO(incidenciaGuardada);
        verify(assembler).toModel(respuestaDTO);
    }

    @Test
    void buscarIncidenciaPorId_Status200() throws Exception {
        Long idBuscado = 1L;

        Incidencia incidencia = new Incidencia();
        incidencia.setId(idBuscado);
        incidencia.setTitulo(faker.lorem().sentence());
        incidencia.setDescripcion(faker.lorem().paragraph());

        IncidenciaRespuestaDTO dto = new IncidenciaRespuestaDTO();
        dto.setId(idBuscado);
        dto.setTitulo(incidencia.getTitulo());
        dto.setDescripcion(incidencia.getDescripcion());

        EntityModel<IncidenciaRespuestaDTO> recurso = EntityModel.of(dto);

        // Paso 5: Mock comportamiento del servicio, mapper y assembler
        when(incidenciaService.findById(idBuscado)).thenReturn(incidencia);
        when(mapper.toDTO(incidencia)).thenReturn(dto);
        when(assembler.toModel(dto)).thenReturn(recurso);

        // Paso 6: Ejecutar GET /api/incidencias/{id} y esperar 200 OK
        mockMvc.perform(get("/api/incidencias/{id}", idBuscado))
                .andExpect(status().isOk());

        verify(incidenciaService).findById(idBuscado);
        verify(mapper).toDTO(incidencia);
        verify(assembler).toModel(dto);
    }

    @Test
    void buscarIncidenciaPorId_NoEncontrado_Status404() throws Exception {
        // Paso 1: Id que no existe
        Long idNoExiste = 999L;

        // Paso 2: Simular excepción al buscar un id inexistente
        when(incidenciaService.findById(idNoExiste)).thenThrow(new RuntimeException("Incidencia no encontrada"));

        // Paso 3: Ejecutar GET /api/incidencias/{id} y esperar 404 Not Found
        mockMvc.perform(get("/api/incidencias/{id}", idNoExiste))
                .andExpect(status().isNotFound());

        verify(incidenciaService).findById(idNoExiste);
        verifyNoInteractions(mapper, assembler);
    }

    @Test
    void actualizarIncidencia_Status200() throws Exception {
        // Paso 1: Id de la incidencia a actualizar
        Long idActualizar = 1L;

        IncidenciaCrearDTO dtoEntrada = new IncidenciaCrearDTO();
        dtoEntrada.setTitulo(faker.lorem().sentence());
        dtoEntrada.setDescripcion(faker.lorem().paragraph());

        Incidencia incidenciaExistente = new Incidencia();
        incidenciaExistente.setId(idActualizar);
        incidenciaExistente.setTitulo("Titulo viejo");
        incidenciaExistente.setDescripcion("Descripcion vieja");

        doAnswer(invocation -> {
            Incidencia incidencia = invocation.getArgument(0);
            IncidenciaCrearDTO dto = invocation.getArgument(1);
            incidencia.setTitulo(dto.getTitulo());
            incidencia.setDescripcion(dto.getDescripcion());
            return null;
        }).when(mapper).updateEntity(any(Incidencia.class), any(IncidenciaCrearDTO.class));

        // Paso 5: Entidad despues de actualizar
        Incidencia incidenciaActualizada = new Incidencia();
        incidenciaActualizada.setId(idActualizar);
        incidenciaActualizada.setTitulo(dtoEntrada.getTitulo());
        incidenciaActualizada.setDescripcion(dtoEntrada.getDescripcion());

        // Paso 6: DTO de respuesta despues de actualizar
        IncidenciaRespuestaDTO respuestaDTO = new IncidenciaRespuestaDTO();
        respuestaDTO.setId(idActualizar);
        respuestaDTO.setTitulo(dtoEntrada.getTitulo());
        respuestaDTO.setDescripcion(dtoEntrada.getDescripcion());

        EntityModel<IncidenciaRespuestaDTO> recurso = EntityModel.of(respuestaDTO);

        when(incidenciaService.findById(idActualizar)).thenReturn(incidenciaExistente);
        when(incidenciaService.save(incidenciaExistente)).thenReturn(incidenciaActualizada);
        when(mapper.toDTO(incidenciaActualizada)).thenReturn(respuestaDTO);
        when(assembler.toModel(respuestaDTO)).thenReturn(recurso);

        // Paso 9: Serializar DTO de entrada a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dtoEntrada);

        // Paso 10: Ejecutar PUT /api/incidencias/{id} y esperar 200 OK
        mockMvc.perform(put("/api/incidencias/{id}", idActualizar)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarIncidencia_Status204() throws Exception {
        Long idEliminar = 1L;

        doNothing().when(incidenciaService).delete(idEliminar);

        // Paso 3: Ejecutar DELETE /api/incidencias/{id} y esperar 204 No Content
        mockMvc.perform(delete("/api/incidencias/{id}", idEliminar))
                .andExpect(status().isNoContent());

        verify(incidenciaService).delete(idEliminar);
    }

    @Test
    void eliminarIncidencia_NoEncontrado_Status404() throws Exception {
        // Paso 1: Id que no existe para eliminar
        Long idNoExiste = 999L;

        // Paso 2: Simular excepcion cuando se intenta eliminar un id inexistente
        doThrow(new RuntimeException("Incidencia no encontrada")).when(incidenciaService).delete(idNoExiste);

        // Paso 3: Ejecutar DELETE y esperar 404 Not Found
        mockMvc.perform(delete("/api/incidencias/{id}", idNoExiste))
                .andExpect(status().isNotFound());

        verify(incidenciaService).delete(idNoExiste);
        verifyNoInteractions(mapper, assembler);
    }
}
