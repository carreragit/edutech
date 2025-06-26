package com.usarios.hateoas;

import com.usarios.dto.UsuarioRespuestaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;
//si se cambia la ruta, el nombre del link o la lógica de assembler accidentalmente, el test fallará y te avisará.
class UsuarioAssemblerTest {
    //crea una instancia del assembler a probar
    private final UsuarioAssembler assembler = new UsuarioAssembler();

    @Test
    void toModel_debeAgregarLinksCorrectos() { // toModel genera los links correctos.
        // crea un DTO de ejemplo.
        UsuarioRespuestaDTO dto = new UsuarioRespuestaDTO();
        dto.setId(15L);
        dto.setNombreUsuario("Mario");

        // ejecuta el metodo a testear
        // llama al assembler para obtener el EntityModel.
        EntityModel<UsuarioRespuestaDTO> model = assembler.toModel(dto);

        // verifica que el contenido del EntityModel es el DTO original
        // así sabemos que no se modificó ni perdió ningún dato.
        assertEquals(dto, model.getContent());

        // verifica que existe el link "self con la URL correcta
        Link selfLink = model.getLink("self").orElse(null);
        assertNotNull(selfLink); // verifica que el link existe (no es null).
        System.out.println("selfLink.getHref() = " + selfLink.getHref());
        assertEquals("/api/v1/usuarios/15", selfLink.getHref());

        // verifica que existe el link "usuarios"
        Link usuariosLink = model.getLink("usuarios").orElse(null);
        assertNotNull(usuariosLink); // Verifica que el link existe (no es null).
        System.out.println("usuariosLink.getHref() = " + usuariosLink.getHref());
        assertEquals("/api/v1/usuarios", usuariosLink.getHref()); // Verifica que la URL contiene /auth (ruta base del listado).

    }
}