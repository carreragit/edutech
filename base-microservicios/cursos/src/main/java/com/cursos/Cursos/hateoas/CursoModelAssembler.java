package com.cursos.Cursos.hateoas;

import com.cursos.Cursos.controller.CursoController;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CursoModelAssembler implements RepresentationModelAssembler<CursoRespuestaDTO, EntityModel<CursoRespuestaDTO>> {

    @Override
    public EntityModel<CursoRespuestaDTO> toModel(CursoRespuestaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CursoController.class).buscar(dto.getId())).withSelfRel(),
                linkTo(methodOn(CursoController.class).listar()).withRel("cursos"),
                linkTo(methodOn(CursoController.class).eliminar(dto.getId())).withRel("eliminar"),
                linkTo(methodOn(CursoController.class).actualizar(dto.getId(), null)).withRel("actualizar")
        );
    }
}
