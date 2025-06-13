package com.cursos.Cursos.controller;

import com.cursos.Cursos.dto.CursoCrearDTO;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import com.cursos.Cursos.hateoas.CursoModelAssembler;
import com.cursos.Cursos.mapper.CursoMapper;
import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoMapper cursoMapper;

    // Nuevo: Inyección del assembler que crea los recursos HATEOAS
    @Autowired
    private CursoModelAssembler assembler;

    // Cambio: Ahora el método retorna un CollectionModel de EntityModel (HATEOAS)
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CursoRespuestaDTO>>> listar() {
        List<Curso> cursos = cursoService.findAll();
        if (cursos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Se mapean los DTOs a recursos HATEOAS con assembler.toModel()
        List<EntityModel<CursoRespuestaDTO>> cursosModel = cursos.stream()
                .map(curso -> assembler.toModel(cursoMapper.toDTO(curso)))
                .collect(Collectors.toList());

        // Se crea un CollectionModel que incluye el self link al recurso listar
        CollectionModel<EntityModel<CursoRespuestaDTO>> collectionModel = CollectionModel.of(cursosModel,
                linkTo(methodOn(CursoController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // Cambio: La respuesta ahora es un EntityModel que contiene el DTO y enlaces HATEOAS
    @PostMapping
    public ResponseEntity<EntityModel<CursoRespuestaDTO>> guardar(@RequestBody CursoCrearDTO dto) {
        Curso curso = cursoMapper.toEntity(dto);
        Curso creado = cursoService.save(curso);
        CursoRespuestaDTO respuestaDTO = cursoMapper.toDTO(creado);

        // Se agrega cabecera Location con enlace al nuevo recurso creado y se retorna el EntityModel
        return ResponseEntity
                .created(linkTo(methodOn(CursoController.class).buscar(creado.getId())).toUri())
                .body(assembler.toModel(respuestaDTO));
    }

    // Cambio: Devuelve un EntityModel para el recurso solicitado con enlaces HATEOAS
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CursoRespuestaDTO>> buscar(@PathVariable("id") Long id) {
        try {
            Curso curso = cursoService.findById(id);
            CursoRespuestaDTO dto = cursoMapper.toDTO(curso);
            return ResponseEntity.ok(assembler.toModel(dto)); // Uso del assembler
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Cambio: Devuelve un EntityModel con el recurso actualizado y enlaces HATEOAS
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CursoRespuestaDTO>> actualizar(@PathVariable("id") Long id,
                                                                     @RequestBody CursoCrearDTO dto) {
        try {
            Curso curso = cursoService.findById(id);
            cursoMapper.updateEntity(curso, dto);
            Curso actualizado = cursoService.save(curso);
            CursoRespuestaDTO respuestaDTO = cursoMapper.toDTO(actualizado);

            return ResponseEntity.ok(assembler.toModel(respuestaDTO)); // Uso del assembler
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // No cambia: sigue devolviendo solo status (no content o not found)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
