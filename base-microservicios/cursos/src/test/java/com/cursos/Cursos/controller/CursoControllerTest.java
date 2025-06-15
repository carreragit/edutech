package com.cursos.Cursos.controller;

import com.cursos.Cursos.dto.CursoCrearDTO;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import com.cursos.Cursos.hateoas.CursoModelAssembler;
import com.cursos.Cursos.mapper.CursoMapper;
import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class CursoControllerTest {

    @InjectMocks
    private CursoController cursoController;

    @Mock
    private CursoService cursoService;

    @Mock
    private CursoMapper cursoMapper;

    @Mock
    private CursoModelAssembler assembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void listar() {
        // 1. Crear objetos Curso simulados
        Curso curso1 = new Curso();
        curso1.setId(1L);
        Curso curso2 = new Curso();
        curso2.setId(2L);

        // 2. Cuando se llame al servicio findAll, que devuelva esos cursos
        when(cursoService.findAll()).thenReturn(Arrays.asList(curso1, curso2));

        // 3. Simular la conversión de entidades a DTOs
        CursoRespuestaDTO dto1 = new CursoRespuestaDTO();
        CursoRespuestaDTO dto2 = new CursoRespuestaDTO();

        when(cursoMapper.toDTO(curso1)).thenReturn(dto1);
        when(cursoMapper.toDTO(curso2)).thenReturn(dto2);

        // 4. Simular la conversión de DTOs a EntityModel (HATEOAS)
        EntityModel<CursoRespuestaDTO> model1 = EntityModel.of(dto1);
        EntityModel<CursoRespuestaDTO> model2 = EntityModel.of(dto2);

        when(assembler.toModel(dto1)).thenReturn(model1);
        when(assembler.toModel(dto2)).thenReturn(model2);

        // 5. Ejecutar el metodo listar() del controlador
        ResponseEntity<CollectionModel<EntityModel<CursoRespuestaDTO>>> response = cursoController.listar();

        // 6. Verificar que el resultado sea 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // 7. Verificar que el cuerpo no esté vacío y tenga 2 elementos
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());

        // 8. Verificar que el servicio findAll fue llamado exactamente una vez
        verify(cursoService, times(1)).findAll();

    }

    @Test
    void guardar() {
        // 1. Crear un DTO de entrada con datos ficticios
        CursoCrearDTO dto = new CursoCrearDTO();
        // aquí podrías setear campos si quieres (opcional)

        // 2. Crear la entidad que espera recibir el servicio
        Curso cursoEntity = new Curso();
        // también setea campos si quieres

        // 3. Crear la entidad que devuelve el servicio al guardar (con ID generado)
        Curso cursoGuardado = new Curso();
        cursoGuardado.setId(1L);

        // 4. Crear el DTO de respuesta que el mapper genera después de guardar
        CursoRespuestaDTO respuestaDTO = new CursoRespuestaDTO();
        // setear campos si quieres

        // 5. Simular comportamiento del mapper: dto a entidad
        when(cursoMapper.toEntity(dto)).thenReturn(cursoEntity);

        // 6. Simular comportamiento del servicio: guarda y devuelve entidad con ID
        when(cursoService.save(cursoEntity)).thenReturn(cursoGuardado);

        // 7. Simular comportamiento del mapper: entidad guardada a DTO de respuesta
        when(cursoMapper.toDTO(cursoGuardado)).thenReturn(respuestaDTO);

        // 8. Simular el assembler para crear el modelo HATEOAS
        EntityModel<CursoRespuestaDTO> model = EntityModel.of(respuestaDTO);
        when(assembler.toModel(respuestaDTO)).thenReturn(model);

        // 9. Ejecutar el metodo guardar() del controlador
        ResponseEntity<EntityModel<CursoRespuestaDTO>> response = cursoController.guardar(dto);

        // 10. Verificar que el código de respuesta sea 201 (Created)
        assertEquals(201, response.getStatusCodeValue());

        // 11. Verificar que el cuerpo no sea nulo y contenga el modelo esperado
        assertNotNull(response.getBody());
        assertEquals(model, response.getBody());

        // 12. Verificar que el Location header contenga la URI con el ID del nuevo recurso
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/v1/cursos/1"));

        // 13. Verificar que los mocks fueron llamados una vez
        verify(cursoMapper, times(1)).toEntity(dto);
        verify(cursoService, times(1)).save(cursoEntity);
        verify(cursoMapper, times(1)).toDTO(cursoGuardado);
        verify(assembler, times(1)).toModel(respuestaDTO);

    }

    @Test
    void buscar() {
        // 1. Definimos el ID que queremos buscar
        Long id = 1L;

        // 2. Creamos una entidad Curso simulada con ese ID
        Curso curso = new Curso();
        curso.setId(id);

        // 3. Simulamos que el servicio encuentra el curso con ese ID
        when(cursoService.findById(id)).thenReturn(curso);

        // 4. Creamos el DTO de respuesta que el mapper generaría
        CursoRespuestaDTO dto = new CursoRespuestaDTO();

        // 5. Simulamos que el mapper convierte la entidad a DTO
        when(cursoMapper.toDTO(curso)).thenReturn(dto);

        // 6. Simulamos que el assembler convierte el DTO a modelo HATEOAS
        EntityModel<CursoRespuestaDTO> model = EntityModel.of(dto);
        when(assembler.toModel(dto)).thenReturn(model);

        // 7. Ejecutamos el metodo buscar() del controlador
        ResponseEntity<EntityModel<CursoRespuestaDTO>> response = cursoController.buscar(id);

        // 8. Verificamos que el código HTTP sea 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // 9. Verificamos que el cuerpo no sea nulo y sea igual al modelo esperado
        assertNotNull(response.getBody());
        assertEquals(model, response.getBody());

        // 10. Verificamos que el servicio y mapper fueron llamados una vez
        verify(cursoService, times(1)).findById(id);
        verify(cursoMapper, times(1)).toDTO(curso);
        verify(assembler, times(1)).toModel(dto);

    }

    @Test
    void buscarNoEncontrado() {
        Long id = 1L;

        // Simulamos que el servicio lanza excepción (curso no encontrado)
        when(cursoService.findById(id)).thenThrow(new RuntimeException());

        // Ejecutamos
        ResponseEntity<EntityModel<CursoRespuestaDTO>> response = cursoController.buscar(id);

        // Verificamos que el código sea 404 Not Found
        assertEquals(404, response.getStatusCodeValue());

        // Verificamos que el cuerpo sea nulo (no hay contenido)
        assertNull(response.getBody());

        // Verificamos que el servicio fue llamado una vez
        verify(cursoService, times(1)).findById(id);
    }


    @Test
    void actualizar() {
        // 1. Definimos el ID y el DTO con los datos a actualizar
        Long id = 1L;
        CursoCrearDTO dto = new CursoCrearDTO();
        // (Puedes setear campos en dto si quieres simular datos reales)

        // 2. Creamos la entidad existente simulada que devuelve findById
        Curso cursoExistente = new Curso();
        cursoExistente.setId(id);

        // 3. Simulamos que el servicio encuentra el curso por ID
        when(cursoService.findById(id)).thenReturn(cursoExistente);

        // 4. El mapper actualiza la entidad con los datos del DTO (método void)
        doAnswer(invocation -> {
            Curso cursoArg = invocation.getArgument(0);
            CursoCrearDTO dtoArg = invocation.getArgument(1);
            // Simulación simple: por ejemplo, actualizar un campo si quieres
            // cursoArg.setNombre(dtoArg.getNombre());
            return null;
        }).when(cursoMapper).updateEntity(cursoExistente, dto);

        // 5. Simulamos que el servicio guarda la entidad actualizada y la devuelve
        Curso cursoActualizado = new Curso();
        cursoActualizado.setId(id);
        when(cursoService.save(cursoExistente)).thenReturn(cursoActualizado);

        // 6. Simulamos que el mapper convierte la entidad actualizada a DTO de respuesta
        CursoRespuestaDTO respuestaDTO = new CursoRespuestaDTO();
        when(cursoMapper.toDTO(cursoActualizado)).thenReturn(respuestaDTO);

        // 7. Simulamos que el assembler convierte el DTO en modelo HATEOAS
        EntityModel<CursoRespuestaDTO> model = EntityModel.of(respuestaDTO);
        when(assembler.toModel(respuestaDTO)).thenReturn(model);

        // 8. Ejecutamos el método actualizar() del controlador
        ResponseEntity<EntityModel<CursoRespuestaDTO>> response = cursoController.actualizar(id, dto);

        // 9. Verificamos que el código HTTP sea 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // 10. Verificamos que el cuerpo no sea nulo y sea igual al modelo esperado
        assertNotNull(response.getBody());
        assertEquals(model, response.getBody());

        // 11. Verificamos que los mocks se llamaron correctamente
        verify(cursoService, times(1)).findById(id);
        verify(cursoMapper, times(1)).updateEntity(cursoExistente, dto);
        verify(cursoService, times(1)).save(cursoExistente);
        verify(cursoMapper, times(1)).toDTO(cursoActualizado);
        verify(assembler, times(1)).toModel(respuestaDTO);

    }

    @Test
    void actualizarNoEncontrado() {
        Long id = 1L;
        CursoCrearDTO dto = new CursoCrearDTO();

        // Simulamos que findById lanza excepción porque no existe
        when(cursoService.findById(id)).thenThrow(new RuntimeException());

        // Ejecutamos
        ResponseEntity<EntityModel<CursoRespuestaDTO>> response = cursoController.actualizar(id, dto);

        // Verificamos que el status sea 404
        assertEquals(404, response.getStatusCodeValue());

        // El cuerpo debe ser nulo
        assertNull(response.getBody());

        // Verificamos que findById fue llamado una vez
        verify(cursoService, times(1)).findById(id);
    }



    @Test
    void eliminar() {
        Long id = 1L;

        // No hacemos when porque delete es void y no devuelve nada
        // Solo verificamos que no lance excepción

        // Ejecutamos el método eliminar del controlador
        ResponseEntity<?> response = cursoController.eliminar(id);

        // Verificamos que el status sea 204 No Content
        assertEquals(204, response.getStatusCodeValue());

        // Verificamos que el cuerpo sea nulo
        assertNull(response.getBody());

        // Verificamos que el método delete del servicio se llamó una vez con el ID
        verify(cursoService, times(1)).delete(id);

    }

    @Test
    void eliminarNoEncontrado() {
        Long id = 1L;

        // Simulamos que el servicio lanza excepción al intentar eliminar
        doThrow(new RuntimeException()).when(cursoService).delete(id);

        // Ejecutamos
        ResponseEntity<?> response = cursoController.eliminar(id);

        // Verificamos que el status sea 404 Not Found
        assertEquals(404, response.getStatusCodeValue());

        // Verificamos que el cuerpo sea nulo
        assertNull(response.getBody());

        // Verificamos que el método delete fue llamado una vez
        verify(cursoService, times(1)).delete(id);
    }

}