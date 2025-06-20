package com.cursos.Cursos.controller;

import com.cursos.Cursos.dto.CursoCrearDTO;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import com.cursos.Cursos.hateoas.CursoModelAssembler;
import com.cursos.Cursos.mapper.CursoMapper;
import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private CursoMapper cursoMapper;

    @MockBean
    private CursoModelAssembler assembler;

    private final Faker faker = new Faker();

    @Test
    void listarCursos_Status200() throws Exception {
        // Paso 1: Crear un curso ficticio
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre(faker.educator().course());
        curso.setDescripcion(faker.lorem().sentence());

        // Paso 2: DTO que representara la respuesta esperada
        CursoRespuestaDTO dto = new CursoRespuestaDTO();
        dto.setId(curso.getId());
        dto.setNombre(curso.getNombre());
        dto.setDescripcion(curso.getDescripcion());

        // Paso 3: EntityModel que representa el recurso con HATEOAS
        EntityModel<CursoRespuestaDTO> recurso = EntityModel.of(
                dto,
                linkTo(methodOn(CursoController.class).buscar(dto.getId())).withSelfRel()
        );

        // Paso 4: Mock del service y assembler
        when(cursoService.findAll()).thenReturn(Arrays.asList(curso));
        when(cursoMapper.toDTO(curso)).thenReturn(dto);
        when(assembler.toModel(dto)).thenReturn(recurso);

        // Paso 5: Ejecutar GET y verificar que el status es 200
        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk());

    }

    @Test
    void listarCursos_SinCursos_retorna204() throws Exception {
        when(cursoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isNoContent());

        verify(cursoService).findAll();
        verifyNoInteractions(cursoMapper, assembler);
    }

    @Test
    void guardarCurso_Status201() throws Exception {
        CursoCrearDTO crearDTO = new CursoCrearDTO();
        crearDTO.setNombre(faker.educator().course());
        crearDTO.setDescripcion(faker.lorem().sentence());

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre(crearDTO.getNombre());
        curso.setDescripcion(crearDTO.getDescripcion());

        CursoRespuestaDTO respuestaDTO = new CursoRespuestaDTO();
        respuestaDTO.setId(curso.getId());
        respuestaDTO.setNombre(curso.getNombre());
        respuestaDTO.setDescripcion(curso.getDescripcion());

        EntityModel<CursoRespuestaDTO> entityModelMock = mock(EntityModel.class);

        when(cursoMapper.toEntity(crearDTO)).thenReturn(curso);
        when(cursoService.save(curso)).thenReturn(curso);
        when(cursoMapper.toDTO(curso)).thenReturn(respuestaDTO);
        when(assembler.toModel(respuestaDTO)).thenReturn(entityModelMock);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(crearDTO);

        mockMvc.perform(post("/api/v1/cursos")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(cursoMapper).toEntity(crearDTO);
        verify(cursoService).save(curso);
        verify(cursoMapper).toDTO(curso);
        verify(assembler).toModel(respuestaDTO);
    }

    @Test
    void buscarCursoPorId_Status200() throws Exception {
        Long idBuscado = 1L;

        Curso curso = new Curso();
        curso.setId(idBuscado);
        curso.setNombre(faker.educator().course());
        curso.setDescripcion(faker.lorem().sentence());

        CursoRespuestaDTO dto = new CursoRespuestaDTO();
        dto.setId(idBuscado);
        dto.setNombre(curso.getNombre());
        dto.setDescripcion(curso.getDescripcion());

        EntityModel<CursoRespuestaDTO> entityModelMock = mock(EntityModel.class);

        when(cursoService.findById(idBuscado)).thenReturn(curso);
        when(cursoMapper.toDTO(curso)).thenReturn(dto);
        when(assembler.toModel(dto)).thenReturn(entityModelMock);

        mockMvc.perform(get("/api/v1/cursos/{id}", idBuscado))
                .andExpect(status().isOk());

        verify(cursoService).findById(idBuscado);
        verify(cursoMapper).toDTO(curso);
        verify(assembler).toModel(dto);
    }

    @Test
    void buscarCursoPorId_NoEncontrado_Status404() throws Exception {
        Long idNoExiste = 999L;

        when(cursoService.findById(idNoExiste)).thenThrow(new RuntimeException("Curso no encontrado"));

        mockMvc.perform(get("/api/v1/cursos/{id}", idNoExiste))
                .andExpect(status().isNotFound());

        verify(cursoService).findById(idNoExiste);
        verifyNoInteractions(cursoMapper, assembler);
    }

    @Test
    void actualizarCurso_Status200() throws Exception {
        // Paso 1: Datos de entrada
        Long idCurso = 1L;

        CursoCrearDTO dtoEntrada = new CursoCrearDTO();
        dtoEntrada.setNombre(faker.educator().course());
        dtoEntrada.setDescripcion(faker.lorem().sentence());

        // Paso 2: Curso existente (simula que ya esta en BD)
        Curso cursoExistente = new Curso();
        cursoExistente.setId(idCurso);
        cursoExistente.setNombre("Nombre Antiguo");
        cursoExistente.setDescripcion("Descripcion antigua");

        // Paso 3: Simular comportamiento del mapper.updateEntity (sin llamar recursivamente al mock)
        doAnswer(invoc -> {
            Curso curso = invoc.getArgument(0);
            CursoCrearDTO dto = invoc.getArgument(1);
            curso.setNombre(dto.getNombre());
            curso.setDescripcion(dto.getDescripcion());
            return null;
        }).when(cursoMapper).updateEntity(any(Curso.class), any(CursoCrearDTO.class));

        // Paso 4: Curso después de ser "actualizado"
        Curso cursoActualizado = new Curso();
        cursoActualizado.setId(idCurso);
        cursoActualizado.setNombre(dtoEntrada.getNombre());
        cursoActualizado.setDescripcion(dtoEntrada.getDescripcion());

        // Paso 5: DTO de respuesta
        CursoRespuestaDTO respuestaDTO = new CursoRespuestaDTO();
        respuestaDTO.setId(cursoActualizado.getId());
        respuestaDTO.setNombre(cursoActualizado.getNombre());
        respuestaDTO.setDescripcion(cursoActualizado.getDescripcion());

        EntityModel<CursoRespuestaDTO> recurso = EntityModel.of(respuestaDTO);

        // Paso 6: Simular comportamientos del servicio y assembler
        when(cursoService.findById(idCurso)).thenReturn(cursoExistente);
        when(cursoService.save(cursoExistente)).thenReturn(cursoActualizado);
        when(cursoMapper.toDTO(cursoActualizado)).thenReturn(respuestaDTO);
        when(assembler.toModel(respuestaDTO)).thenReturn(recurso);

        // Paso 7: Ejecutar PUT y verificar resultado
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dtoEntrada);

        mockMvc.perform(put("/api/v1/cursos/{id}", idCurso)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());

    }

    @Test
    void eliminarCurso_Status204() throws Exception {
        Long idEliminar = 1L;

        doNothing().when(cursoService).delete(idEliminar);

        mockMvc.perform(delete("/api/v1/cursos/{id}", idEliminar))
                .andExpect(status().isNoContent());

        verify(cursoService).delete(idEliminar);
    }

    @Test
    void eliminarCurso_NoEncontrado_Status404() throws Exception {
        Long idNoExiste = 999L;

        doThrow(new RuntimeException("Curso no encontrado")).when(cursoService).delete(idNoExiste);

        mockMvc.perform(delete("/api/v1/cursos/{id}", idNoExiste))
                .andExpect(status().isNotFound());

        verify(cursoService).delete(idNoExiste);
        verifyNoInteractions(cursoMapper, assembler);
    }
}
