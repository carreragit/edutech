package com.soporte1.service;

import com.soporte1.model.Incidencia;
import com.soporte1.repository.IncidenciaRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceTest {

    @Mock
    private IncidenciaRepository incidenciaRepository;

    @InjectMocks
    private IncidenciaService incidenciaService;

    private final Faker faker = new Faker();

    @Test
    void findAll_retornaListaDeIncidencias() {
        // Crear dos incidencias falsas con datos de Faker
        Incidencia inc1 = new Incidencia();
        inc1.setId(1L);
        inc1.setTitulo(faker.lorem().sentence());
        inc1.setDescripcion(faker.lorem().paragraph());

        Incidencia inc2 = new Incidencia();
        inc2.setId(2L);
        inc2.setTitulo(faker.lorem().sentence());
        inc2.setDescripcion(faker.lorem().paragraph());

        // Simular que el repositorio devuelve estas incidencias
        List<Incidencia> lista = Arrays.asList(inc1, inc2);
        when(incidenciaRepository.findAll()).thenReturn(lista);

        // Ejecutar el metodo del service
        List<Incidencia> resultado = incidenciaService.findAll();

        // Verificar que el resultado tenga la cantidad esperada y datos correctos
        assertEquals(2, resultado.size());
        assertEquals(inc1.getTitulo(), resultado.get(0).getTitulo());
        assertEquals(inc2.getDescripcion(), resultado.get(1).getDescripcion());

        // Verificar que el repositorio se llamo una sola vez
        verify(incidenciaRepository, times(1)).findAll();
    }

    @Test
    void findById_incidenciaExiste_retornaIncidencia() {
        Long id = 1L;
        // Crear incidencia falsa con id y titulo
        Incidencia incidencia = new Incidencia();
        incidencia.setId(id);
        incidencia.setTitulo(faker.lorem().sentence());

        when(incidenciaRepository.findById(id)).thenReturn(Optional.of(incidencia));

        // Ejecutar el metodo findById del service
        Incidencia resultado = incidenciaService.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(incidencia.getTitulo(), resultado.getTitulo());

        verify(incidenciaRepository, times(1)).findById(id);
    }

    @Test
    void findById_incidenciaNoExiste_lanzaExcepcion() {
        Long id = 999L;

        // Simular que el repositorio no encuentra la incidencia (Optional vacio)
        when(incidenciaRepository.findById(id)).thenReturn(Optional.empty());

        // Verificar que al buscar una incidencia que no existe se lance una excepcion
        assertThrows(NoSuchElementException.class, () -> incidenciaService.findById(id));

        verify(incidenciaRepository, times(1)).findById(id);
    }

    @Test
    void save_guardarIncidencia_retornaIncidencia() {
        Incidencia incidencia = new Incidencia();
        incidencia.setId(1L);
        incidencia.setTitulo(faker.lorem().sentence());

        // Simular que al guardar la incidencia se devuelve la misma entidad
        when(incidenciaRepository.save(incidencia)).thenReturn(incidencia);

        // Ejecutar el metodo save del service
        Incidencia resultado = incidenciaService.save(incidencia);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(incidenciaRepository, times(1)).save(incidencia);
    }

    @Test
    void delete_eliminarIncidencia_llamaAlRepositorio() {
        Long id = 1L;

        // Simular que el repositorio no lanza excepcion al eliminar
        doNothing().when(incidenciaRepository).deleteById(id);

        incidenciaService.delete(id);

        verify(incidenciaRepository, times(1)).deleteById(id);
    }
}
