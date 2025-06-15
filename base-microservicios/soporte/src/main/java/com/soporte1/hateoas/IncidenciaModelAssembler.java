package com.soporte1.hateoas;

import com.soporte1.controller.IncidenciaController;
import com.soporte1.dto.IncidenciaRespuestaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IncidenciaModelAssembler implements RepresentationModelAssembler<IncidenciaRespuestaDTO, EntityModel<IncidenciaRespuestaDTO>> {

    @Override
    public EntityModel<IncidenciaRespuestaDTO> toModel(IncidenciaRespuestaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(IncidenciaController.class).buscar(dto.getId())).withSelfRel(),
                linkTo(methodOn(IncidenciaController.class).listar()).withRel("listar"),
                linkTo(methodOn(IncidenciaController.class).eliminar(dto.getId())).withRel("eliminar"),
                linkTo(methodOn(IncidenciaController.class).actualizar(dto.getId(), null)).withRel("actualizar") // `null` solo para construir el link
        );
    }
}
