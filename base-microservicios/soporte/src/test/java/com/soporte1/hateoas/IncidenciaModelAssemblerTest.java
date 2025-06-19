package com.soporte1.hateoas;

import com.soporte1.dto.IncidenciaRespuestaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;

class IncidenciaModelAssemblerTest {

    // Instancia del assembler que vamos a probar
    private final IncidenciaModelAssembler assembler = new IncidenciaModelAssembler();

    @Test
    void toModel_debeAgregarLinksCorrectos() {
        // Paso 1: Crear DTO de ejemplo con datos de prueba
        IncidenciaRespuestaDTO dto = new IncidenciaRespuestaDTO();
        dto.setId(20L);
        dto.setTitulo("Incidencia ejemplo");
        dto.setDescripcion("Descripción ejemplo de incidencia");

        // Paso 2: Ejecutar el metodo toModel para obtener el EntityModel con enlaces HATEOAS
        EntityModel<IncidenciaRespuestaDTO> model = assembler.toModel(dto);

        // Paso 3: Verificar que el contenido del EntityModel es el DTO original
        assertEquals(dto, model.getContent());

        // Paso 4: Verificar que existe el link "self" y que apunta al recurso correcto
        Link selfLink = model.getLink("self").orElse(null);
        assertNotNull(selfLink);
        System.out.println("selfLink.getHref() = " + selfLink.getHref());
        assertTrue(selfLink.getHref().endsWith("/api/incidencias/20"));

        // Paso 5: Verificar que existe el link "listar" que apunta a la lista completa
        Link listarLink = model.getLink("listar").orElse(null);
        assertNotNull(listarLink);
        System.out.println("listarLink.getHref() = " + listarLink.getHref());
        assertTrue(listarLink.getHref().endsWith("/api/incidencias"));

        // Paso 6: Verificar que existe el link "eliminar" con la URL correcta
        Link eliminarLink = model.getLink("eliminar").orElse(null);
        assertNotNull(eliminarLink);
        System.out.println("eliminarLink.getHref() = " + eliminarLink.getHref());
        assertTrue(eliminarLink.getHref().endsWith("/api/incidencias/20"));

        // Paso 7: Verificar que existe el link "actualizar" con la URL correcta
        Link actualizarLink = model.getLink("actualizar").orElse(null);
        assertNotNull(actualizarLink);
        System.out.println("actualizarLink.getHref() = " + actualizarLink.getHref());
        assertTrue(actualizarLink.getHref().endsWith("/api/incidencias/20"));
    }
}
