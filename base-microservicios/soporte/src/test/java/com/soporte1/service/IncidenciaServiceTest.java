package com.soporte1.service;

import com.soporte1.model.Incidencia;
import com.soporte1.repository.IncidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidenciaServiceTest {

    @Mock
    private IncidenciaRepository repository;

    @InjectMocks
    private IncidenciaService incidenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        // Crear lista simulada de incidencias
        List<Incidencia> incidenciasMock = new ArrayList<>();
        incidenciasMock.add(new Incidencia());
        incidenciasMock.add(new Incidencia());

        // Simular comportamiento del repository
        when(repository.findAll()).thenReturn(incidenciasMock);

        // Ejecutar metodo real
        List<Incidencia> resultado = incidenciaService.findAll();

        // Verificar
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        Incidencia incidenciaMock = new Incidencia();
        incidenciaMock.setId(1L);

        // Simular comportamiento del repository
        when(repository.findById(1L)).thenReturn(Optional.of(incidenciaMock));

        // Ejecutar metodo real
        Incidencia resultado = incidenciaService.findById(1L);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void save() {
        Incidencia incidenciaParaGuardar = new Incidencia();
        incidenciaParaGuardar.setTitulo("Error grave");

        // Crear incidencia que simula estar guardada en base de datos
        Incidencia incidenciaGuardada = new Incidencia();
        incidenciaGuardada.setId(1L);
        incidenciaGuardada.setTitulo("Error grave");

        // Simular comportamiento del repository
        when(repository.save(incidenciaParaGuardar)).thenReturn(incidenciaGuardada);

        // Ejecutar metodo real
        Incidencia resultado = incidenciaService.save(incidenciaParaGuardar);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Error grave", resultado.getTitulo());
        verify(repository, times(1)).save(incidenciaParaGuardar);
    }

    @Test
    void delete() {
        Long id = 1L;

        // Simular comportamiento del repository (void)
        doNothing().when(repository).deleteById(id);


        incidenciaService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

}
