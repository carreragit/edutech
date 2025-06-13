package com.cursos.Cursos.mapper;

import com.cursos.Cursos.dto.CursoCrearDTO;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import com.cursos.Cursos.model.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoRespuestaDTO toDTO(Curso curso) {
        CursoRespuestaDTO dto = new CursoRespuestaDTO();
        dto.setId(curso.getId());
        dto.setNombre(curso.getNombre());
        dto.setProfesor(curso.getProfesor());
        dto.setDescripcion(curso.getDescripcion());
        dto.setFormato(curso.getFormato());
        return dto;
    }

    public Curso toEntity(CursoCrearDTO dto) {
        Curso curso = new Curso();
        curso.setNombre(dto.getNombre());
        curso.setProfesor(dto.getProfesor());
        curso.setDescripcion(dto.getDescripcion());
        curso.setFormato(dto.getFormato());
        return curso;
    }

    public void updateEntity(Curso curso, CursoCrearDTO dto) {
        curso.setNombre(dto.getNombre());
        curso.setProfesor(dto.getProfesor());
        curso.setDescripcion(dto.getDescripcion());
        curso.setFormato(dto.getFormato());
    }
}
