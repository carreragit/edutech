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
        incidencia.setTitulo("Título de prueba");
        incidencia.setDescripcion("Descripción de prueba");
        incidencia.setEstado("Estado de prueba");
        incidencia.setUsuarioId(100L);

        // Paso 2: Mapear entidad a DTO
        IncidenciaRespuestaDTO dto = mapper.toDTO(incidencia);

        // Paso 3: Verificar que todos los campos se mapearon correctamente
        assertEquals(incidencia.getId(), dto.getId());
        assertEquals(incidencia.getTitulo(), dto.getTitulo());
        assertEquals(incidencia.getDescripcion(), dto.getDescripcion());
        assertEquals(incidencia.getEstado(), dto.getEstado());
        assertEquals(incidencia.getUsuarioId(), dto.getUsuarioId());
    }

    @Test
    void toEntity_debeMapearCamposCorrectamente() {
        // Paso 1: Crear DTO con datos de ejemplo
        IncidenciaCrearDTO dto = new IncidenciaCrearDTO();
        dto.setTitulo("Título nuevo");
        dto.setDescripcion("Descripción nueva");
        dto.setEstado("Estado nuevo");
        dto.setUsuarioId(200L);

        // Paso 2: Mapear DTO a entidad
        Incidencia incidencia = mapper.toEntity(dto);

        // Paso 3: Verificar que todos los campos se mapearon correctamente
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
        incidencia.setTitulo("Título original");
        incidencia.setDescripcion("Descripción original");
        incidencia.setEstado("Estado original");
        incidencia.setUsuarioId(100L);

        // Paso 2: Crear DTO con datos nuevos para actualizar
        IncidenciaCrearDTO dto = new IncidenciaCrearDTO();
        dto.setTitulo("Título actualizado");
        dto.setDescripcion("Descripción actualizada");
        dto.setEstado("Estado actualizado");
        dto.setUsuarioId(300L);

        // Paso 3: Actualizar entidad con los datos del DTO
        mapper.updateEntity(incidencia, dto);

        // Paso 4: Verificar que la entidad fue actualizada correctamente
        assertEquals("Título actualizado", incidencia.getTitulo());
        assertEquals("Descripción actualizada", incidencia.getDescripcion());
        assertEquals("Estado actualizado", incidencia.getEstado());
        assertEquals(300L, incidencia.getUsuarioId());

        // Paso 5: Verificar que el id no se modificó
        assertEquals(10L, incidencia.getId());
    }
}
