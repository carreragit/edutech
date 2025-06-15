package com.auth.hateoas;

import com.auth.controller.AuthController;
import com.auth.Dto.LoginResponseDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class LoginResponseAssembler extends RepresentationModelAssemblerSupport<Boolean, LoginResponseDTO> {

    public LoginResponseAssembler() {
        super(AuthController.class, LoginResponseDTO.class);
    }

    @Override
    public LoginResponseDTO toModel(Boolean success) {
        String message = success ? "Login correcto" : "Login incorrecto";
        LoginResponseDTO dto = new LoginResponseDTO(success, message);

        dto.add(linkTo(methodOn(AuthController.class).login(null)).withSelfRel());
        // Puedes agregar más enlaces relevantes aquí, por ejemplo:
        // dto.add(linkTo(methodOn(OtherController.class).someMethod()).withRel("otro-recurso"));

        return dto;
    }
}
