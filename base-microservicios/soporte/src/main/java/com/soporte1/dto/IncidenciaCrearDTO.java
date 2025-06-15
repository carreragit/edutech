package com.soporte1.dto;

import lombok.Data;

@Data
public class IncidenciaCrearDTO {
    private String titulo;
    private String descripcion;
    private String estado;
    private Long usuarioId;
}
