package com.cursos.Cursos.mapper;

import com.cursos.Cursos.dto.CursoCrearDTO;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import com.cursos.Cursos.model.Curso;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CursoMapperTest {

    private final CursoMapper cursoMapper = new CursoMapper();

    @Test
    void toDTO_debeMapearCamposCorrectamente() {
        // Crear entidad Curso con datos iniciales
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso 1");
        curso.setProfesor("Profesor 1");
        curso.setDescripcion("Descripción del curso 1");
        curso.setFormato("Formato 1");

        CursoRespuestaDTO dto = cursoMapper.toDTO(curso);

        // Verificar que todos los campos estén correctamente mapeados
        assertEquals(curso.getId(), dto.getId());
        assertEquals(curso.getNombre(), dto.getNombre());
        assertEquals(curso.getProfesor(), dto.getProfesor());
        assertEquals(curso.getDescripcion(), dto.getDescripcion());
        assertEquals(curso.getFormato(), dto.getFormato());
    }

    @Test
    void toEntity_debeMapearCamposCorrectamente() {
        // Crear DTO con datos nuevos
        CursoCrearDTO dto = new CursoCrearDTO();
        dto.setNombre("Curso nuevo");
        dto.setProfesor("Profesor nuevo");
        dto.setDescripcion("Descripción del curso nuevo");
        dto.setFormato("Formato nuevo");

        Curso curso = cursoMapper.toEntity(dto);

        // Verificar que todos los campos estén correctamente mapeados
        assertEquals(dto.getNombre(), curso.getNombre());
        assertEquals(dto.getProfesor(), curso.getProfesor());
        assertEquals(dto.getDescripcion(), curso.getDescripcion());
        assertEquals(dto.getFormato(), curso.getFormato());
    }

    @Test
    void updateEntity_debeActualizarLosCampos() {
        // Crear entidad Curso con datos iniciales
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso original");
        curso.setProfesor("Profesor original");
        curso.setDescripcion("Descripción original");
        curso.setFormato("Formato original");

        // Crear DTO con nuevos datos para actualizar
        CursoCrearDTO dto = new CursoCrearDTO();
        dto.setNombre("Curso nuevo");
        dto.setProfesor("Profesor nuevo");
        dto.setDescripcion("Descripción del curso nuevo");
        dto.setFormato("Formato nuevo");

        // Actualizar entidad con datos del DTO
        cursoMapper.updateEntity(curso, dto);

        // Verificar que la entidad fue actualizada correctamente, id no debe cambiar
        assertEquals("Curso nuevo", curso.getNombre());
        assertEquals("Profesor nuevo", curso.getProfesor());
        assertEquals("Descripción del curso nuevo", curso.getDescripcion());
        assertEquals("Formato nuevo", curso.getFormato());
        assertEquals(1L, curso.getId());
    }
}
