package com.cursos.Cursos.hateoas;

import com.cursos.Cursos.dto.CursoRespuestaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;

class CursoModelAssemblerTest {

    // Instancia del assembler a probar
    private final CursoModelAssembler assembler = new CursoModelAssembler();

    @Test
    void toModel_debeAgregarLinksCorrectos() {
        // Crear DTO ejemplo
        CursoRespuestaDTO dto = new CursoRespuestaDTO();
        dto.setId(15L);
        dto.setNombre("Curso ejemplo");
        dto.setDescripcion("Descripción ejemplo");

        // Ejecutar método a testear (generar el EntityModel)
        EntityModel<CursoRespuestaDTO> model = assembler.toModel(dto);

        // Verificar que el contenido es el DTO original
        assertEquals(dto, model.getContent());

        // Verificar que existe el link "self" y su URL es correcta
        Link selfLink = model.getLink("self").orElse(null);
        assertNotNull(selfLink);
        System.out.println("selfLink.getHref() = " + selfLink.getHref());
        assertEquals("/api/v1/cursos/15", selfLink.getHref());

        // Verificar que existe el link "cursos" (lista)
        Link cursosLink = model.getLink("cursos").orElse(null);
        assertNotNull(cursosLink);
        System.out.println("cursosLink.getHref() = " + cursosLink.getHref());
        assertEquals("/api/v1/cursos", cursosLink.getHref());

        // Verificar que existe el link "eliminar"
        Link eliminarLink = model.getLink("eliminar").orElse(null);
        assertNotNull(eliminarLink);
        System.out.println("eliminarLink.getHref() = " + eliminarLink.getHref());
        assertEquals("/api/v1/cursos/15", eliminarLink.getHref());

        // Verificar que existe el link "actualizar"
        Link actualizarLink = model.getLink("actualizar").orElse(null);
        assertNotNull(actualizarLink);
        System.out.println("actualizarLink.getHref() = " + actualizarLink.getHref());
        assertEquals("/api/v1/cursos/15", actualizarLink.getHref());
    }
}
