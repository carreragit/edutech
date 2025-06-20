package com.soporte1.mapper;

import com.soporte1.dto.IncidenciaCrearDTO;
import com.soporte1.dto.IncidenciaRespuestaDTO;
import com.soporte1.model.Incidencia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncidenciaMapperTest {

    private final IncidenciaMapper mapper = new IncidenciaMapper();

    @Test
    void toDTO_debeMapearCamposCorrectamente() {
        // Paso 1: Crear entidad Incidencia con datos de ejemplo
        Incidencia incidencia = new Incidencia();
        incidencia.setId(10L);
        incidencia.setTitulo("Titulo de prueba");
        incidencia.setDescripcion("Descripcion de prueba");
        incidencia.setEstado("Estado de prueba");
        incidencia.setUsuarioId(100L);

        // Paso 2: Mapear entidad a DTO
        IncidenciaRespuestaDTO dto = mapper.toDTO(incidencia);

        assertEquals(incidencia.getId(), dto.getId());
        assertEquals(incidencia.getTitulo(), dto.getTitulo());
        assertEquals(incidencia.getDescripcion(), dto.getDescripcion());
        assertEquals(incidencia.getEstado(), dto.getEstado());
        assertEquals(incidencia.getUsuarioId(), dto.getUsuarioId());
    }

    @Test
    void toEntity_debeMapearCamposCorrectamente() {
        IncidenciaCrearDTO dto = new IncidenciaCrearDTO();
        dto.setTitulo("Titulo nuevo");
        dto.setDescripcion("Descripcion nueva");
        dto.setEstado("Estado nuevo");
        dto.setUsuarioId(200L);

        Incidencia incidencia = mapper.toEntity(dto);

        assertEquals(dto.getTitulo(), incidencia.getTitulo());
        assertEquals(dto.getDescripcion(), incidencia.getDescripcion());
        assertEquals(dto.getEstado(), incidencia.getEstado());
        assertEquals(dto.getUsuarioId(), incidencia.getUsuarioId());
    }

    @Test
    void updateEntity_debeActualizarLosCampos() {
        // Paso 1: Crear entidad Incidencia con datos iniciales
        Incidencia incidencia = new Incidencia();
        incidencia.setId(10L);
        incidencia.setTitulo("Titulo original");
        incidencia.setDescripcion("Descripcion original");
        incidencia.setEstado("Estado original");
        incidencia.setUsuarioId(100L);

        // Paso 2: Crear DTO con datos nuevos para actualizar
        IncidenciaCrearDTO dto = new IncidenciaCrearDTO();
        dto.setTitulo("Titulo actualizado");
        dto.setDescripcion("Descripcion actualizada");
        dto.setEstado("Estado actualizado");
        dto.setUsuarioId(300L);

        // Paso 3: Actualizar entidad con los datos del DTO
        mapper.updateEntity(incidencia, dto);

        // Paso 4: Verificar que la entidad fue actualizada correctamente
        assertEquals("Titulo actualizado", incidencia.getTitulo());
        assertEquals("Descripcion actualizada", incidencia.getDescripcion());
        assertEquals("Estado actualizado", incidencia.getEstado());
        assertEquals(300L, incidencia.getUsuarioId());

        // Paso 5: Verificar que el id no se modifico
        assertEquals(10L, incidencia.getId());
    }
}
