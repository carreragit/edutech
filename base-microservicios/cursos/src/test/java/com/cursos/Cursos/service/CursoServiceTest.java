package com.cursos.Cursos.service;

import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findAll() {
        // Crear lista simulada de cursos
        List<Curso> cursosMock = new ArrayList<>();
        cursosMock.add(new Curso());
        cursosMock.add(new Curso());

        // Definir comportamiento del mock
        when(cursoRepository.findAll()).thenReturn(cursosMock);

        // Ejecutar metodo real
        List<Curso> resultado = cursoService.findAll();

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificar que se llamó al metodo del repo
        verify(cursoRepository, times(1)).findAll();

    }

    @Test
    void findById() {
        Curso cursoMock = new Curso();
        cursoMock.setId(1L);

        // Simular que el repo devuelve Optional con cursoMock al buscar por id 1L
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoMock));

        Curso resultado = cursoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(cursoRepository, times(1)).findById(1L);

    }

    @Test
    void save() {
        Curso cursoParaGuardar = new Curso();
        Curso cursoGuardado = new Curso();
        cursoGuardado.setId(1L);

        when(cursoRepository.save(cursoParaGuardar)).thenReturn(cursoGuardado);

        Curso resultado = cursoService.save(cursoParaGuardar);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(cursoRepository, times(1)).save(cursoParaGuardar);

    }

    @Test
    void delete() {
        Long id = 1L;

        // El metodo deleteById es void, así que solo verificamos que se llame
        doNothing().when(cursoRepository).deleteById(id);

        cursoService.delete(id);

        verify(cursoRepository, times(1)).deleteById(id);
    }
}