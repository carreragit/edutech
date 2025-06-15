package com.soporte1.mapper;

import com.soporte1.dto.IncidenciaCrearDTO;
import com.soporte1.dto.IncidenciaRespuestaDTO;
import com.soporte1.model.Incidencia;
import org.springframework.stereotype.Component;

@Component
public class IncidenciaMapper {

    public IncidenciaRespuestaDTO toDTO(Incidencia incidencia) {
        IncidenciaRespuestaDTO dto = new IncidenciaRespuestaDTO();
        dto.setId(incidencia.getId());
        dto.setTitulo(incidencia.getTitulo());
        dto.setDescripcion(incidencia.getDescripcion());
        dto.setEstado(incidencia.getEstado());
        dto.setUsuarioId(incidencia.getUsuarioId());
        return dto;
    }

    public Incidencia toEntity(IncidenciaCrearDTO dto) {
        Incidencia incidencia = new Incidencia();
        incidencia.setTitulo(dto.getTitulo());
        incidencia.setDescripcion(dto.getDescripcion());
        incidencia.setEstado(dto.getEstado());
        incidencia.setUsuarioId(dto.getUsuarioId());
        return incidencia;
    }

    public void updateEntity(Incidencia incidencia, IncidenciaCrearDTO dto) {
        incidencia.setTitulo(dto.getTitulo());
        incidencia.setDescripcion(dto.getDescripcion());
        incidencia.setEstado(dto.getEstado());
        incidencia.setUsuarioId(dto.getUsuarioId());
    }
}
