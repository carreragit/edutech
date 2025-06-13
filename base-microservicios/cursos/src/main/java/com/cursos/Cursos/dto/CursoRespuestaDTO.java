package com.cursos.Cursos.dto;

import lombok.Data;

@Data
public class CursoRespuestaDTO
{
    private Long id;
    private String nombre;
    private String profesor;
    private String descripcion;
    private String formato;
}
