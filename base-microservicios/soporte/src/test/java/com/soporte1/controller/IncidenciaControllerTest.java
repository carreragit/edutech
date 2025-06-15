package com.soporte1.controller;

import com.soporte1.model.Incidencia;
import com.soporte1.service.IncidenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IncidenciaControllerTest {

    @Mock
    private IncidenciaService incidenciaService;

    @InjectMocks
    private IncidenciaController incidenciaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar() {
        // Simula una lista de incidencias como respuesta del servicio
        Incidencia i1 = new Incidencia();
        Incidencia i2 = new Incidencia();
        when(incidenciaService.findAll()).thenReturn(Arrays.asList(i1, i2));

        // Llama al metodo del controlador
        ResponseEntity<List<Incidencia>> response = incidenciaController.listar();

        // Verifica el resultado
        assertEquals(200, response.getStatusCodeValue()); // Código HTTP esperado
        assertEquals(2, response.getBody().size()); // Cantidad esperada de incidencias
        verify(incidenciaService, times(1)).findAll(); // Verifica que se llamó al servicio una vez

    }

    @Test
    void guardar() {
        // Crea una nueva incidencia
        Incidencia nueva = new Incidencia();
        nueva.setTitulo("Prueba");

        // Simula que el servicio guarda la incidencia
        when(incidenciaService.save(nueva)).thenReturn(nueva);

        // Llama al metodo del controlador
        ResponseEntity<Incidencia> response = incidenciaController.guardar(nueva);

        // Verifica el resultado
        assertEquals(201, response.getStatusCodeValue()); // Código HTTP 201 Created
        assertEquals("Prueba", response.getBody().getTitulo()); // Verifica que el título coincide
        verify(incidenciaService, times(1)).save(nueva); // Verifica que se llamó al servicio

    }

    @Test
    void buscar() {
        // Simula una incidencia encontrada por ID
        Incidencia inc = new Incidencia();
        inc.setId(1L);

        // Simula la respuesta del servicio
        when(incidenciaService.findById(1L)).thenReturn(inc);

        // Llama al metodo del controlador
        ResponseEntity<Incidencia> response = incidenciaController.buscar(1L);

        // Verifica el resultado
        assertEquals(200, response.getStatusCodeValue()); // Código HTTP 200 OK
        assertEquals(1L, response.getBody().getId()); // Verifica que el ID coincide

    }

    @Test
    void actualizar() {
        // Incidencia existente en la base de datos
        Incidencia existente = new Incidencia();
        existente.setId(1L);
        existente.setTitulo("Original");

        // Incidencia nueva con datos modificados
        Incidencia nueva = new Incidencia();
        nueva.setTitulo("Modificada");

        // Simula que el servicio encuentra la incidencia
        when(incidenciaService.findById(1L)).thenReturn(existente);
        // Simula que el servicio guarda la incidencia actualizada
        when(incidenciaService.save(any(Incidencia.class))).thenReturn(existente);

        // Llama al metodo del controlador
        ResponseEntity<Incidencia> response = incidenciaController.actualizar(1L, nueva);

        // Verifica el resultado
        assertEquals(200, response.getStatusCodeValue()); // Código HTTP 200 OK
        verify(incidenciaService).findById(1L); // Verifica que se buscó la incidencia
        verify(incidenciaService).save(any(Incidencia.class)); // Verifica que se guardó la actualización

    }

    @Test
    void eliminar() {
        // Simula que el servicio elimina correctamente la incidencia
        doNothing().when(incidenciaService).delete(1L);

        // Llama al metodo del controlador
        ResponseEntity<?> response = incidenciaController.eliminar(1L);

        // Verifica el resultado
        assertEquals(204, response.getStatusCodeValue()); // Código HTTP 204 No Content
        verify(incidenciaService, times(1)).delete(1L); // Verifica que se llamó al método delete

    }
}