package com.cursos.Cursos.service;

import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.repository.CursoRepository;
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
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private final Faker faker = new Faker();

    @Test
    void findAll_retornaListaDeCursos() {
        // Paso 1: Crear 2 cursos ficticios usando Faker
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre(faker.educator().course());

        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombre(faker.educator().course());

        List<Curso> cursos = Arrays.asList(curso1, curso2);

        // Paso 2: Simular el comportamiento del repositorio (findAll devuelve la lista anterior)
        when(cursoRepository.findAll()).thenReturn(cursos);

        // Paso 3: Llamar al metodo del service
        List<Curso> resultado = cursoService.findAll();

        // Paso 4: Verificar que los datos sean correctos
        assertEquals(2, resultado.size());
        assertEquals(curso1.getNombre(), resultado.get(0).getNombre());
        assertEquals(curso2.getNombre(), resultado.get(1).getNombre());

        // Paso 5: Verificar que el repositorio fue llamado una vez
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void findById_cursoExiste_retornaCurso() {
        // Paso 1: Crear curso ficticio con ID conocido
        Long id = 1L;
        Curso curso = new Curso();
        curso.setId(id);
        curso.setNombre(faker.educator().course());

        // Paso 2: Simular que el repositorio encuentra el curso
        when(cursoRepository.findById(id)).thenReturn(Optional.of(curso));

        // Paso 3: Llamar al metodo del service
        Curso resultado = cursoService.findById(id);

        // Paso 4: Verificar que los datos son correctos
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(curso.getNombre(), resultado.getNombre());

        // Paso 5: Verificar que se llamó al repositorio
        verify(cursoRepository, times(1)).findById(id);
    }

    @Test
    void findById_cursoNoExiste_lanzaExcepcion() {
        // Paso 1: Definir un ID que no existe
        Long id = 100L;

        // Paso 2: Simular que el repositorio no encuentra el curso
        when(cursoRepository.findById(id)).thenReturn(Optional.empty());

        // Paso 3: Verificar que se lanza una excepción al buscar un curso inexistente
        assertThrows(NoSuchElementException.class, () -> cursoService.findById(id));

        // Paso 4: Verificar que el repositorio fue llamado una vez
        verify(cursoRepository, times(1)).findById(id);
    }

    @Test
    void save_guardarCurso_retornaCurso() {
        // Paso 1: Crear curso ficticio
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre(faker.educator().course());

        // Paso 2: Simular que al guardar el curso se devuelve el mismo
        when(cursoRepository.save(curso)).thenReturn(curso);

        // Paso 3: Ejecutar el metodo save
        Curso resultado = cursoService.save(curso);

        // Paso 4: Verificar que se devuelve correctamente
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        // Paso 5: Verificar que se llamó al repositorio
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    void delete_eliminarCurso_llamaAlRepositorio() {
        // Paso 1: Definir el ID a eliminar
        Long id = 1L;

        // Paso 2: Simular que el metodo delete no lanza excepción
        doNothing().when(cursoRepository).deleteById(id);

        // Paso 3: Llamar al metodo delete
        cursoService.delete(id);

        // Paso 4: Verificar que se llamo al repositorio una vez
        verify(cursoRepository, times(1)).deleteById(id);
    }
}
