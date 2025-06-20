package com.auth.hateoas;

import com.auth.Dto.LoginResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseAssemblerTest {
    // crea una instancia del assembler que se va a testear.
    private final LoginResponseAssembler assembler = new LoginResponseAssembler(); //Crea una instancia del assembler que se va a testear.




    @Test
    void toModel_loginExitoso_agregaLinkYMensajeCorectos() { // verifica login exitoso
        // llama a toModel con success=true
        LoginResponseDTO dto = assembler.toModel(true);

        // verifica que el DTO tiene success=true y mensaje correcto
        assertTrue(dto.isSuccess());
        assertEquals("Login correcto", dto.getMessage());

        // verifica que se agrega el link self
        Link selfLink = dto.getLink("self").orElse(null); //busca el link "self" en el DTO
        assertNotNull(selfLink); // verifica que existe
        // Imprime el link para ver su valor real
        System.out.println("selfLink.getHref() = " + selfLink.getHref());
        assertTrue(selfLink.getHref().contains("/auth/login"));
    }

    @Test
    void toModel_loginFallido_agregaLinkYMensajeCorrectos() { //verifica login fallido
        // llama a toModel con success=false
        LoginResponseDTO dto = assembler.toModel(false);

        // verifica que el DTO tiene success=false y mensaje correcto
        assertFalse(dto.isSuccess());
        assertEquals("Login incorrecto", dto.getMessage());

        // verifica que se agrega el link self
        Link selfLink = dto.getLink("self").orElse(null);
        assertNotNull(selfLink);
        System.out.println("selfLink.getHref() = " + selfLink.getHref());
        assertTrue(selfLink.getHref().contains("/auth/login"));
    }
}
