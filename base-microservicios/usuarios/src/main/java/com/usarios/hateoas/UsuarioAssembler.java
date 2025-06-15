package com.usarios.hateoas;

import com.usarios.controller.UsuarioController;
import com.usarios.dto.UsuarioRespuestaDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioAssembler extends RepresentationModelAssemblerSupport<UsuarioRespuestaDTO, EntityModel<UsuarioRespuestaDTO>> {

    public UsuarioAssembler() {
        super(UsuarioController.class, (Class<EntityModel<UsuarioRespuestaDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<UsuarioRespuestaDTO> toModel(UsuarioRespuestaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(UsuarioController.class).buscar(dto.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
    }
}
