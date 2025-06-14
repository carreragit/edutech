package com.cursos.Cursos.controller;

import com.cursos.Cursos.dto.CursoCrearDTO;
import com.cursos.Cursos.dto.CursoRespuestaDTO;
import com.cursos.Cursos.hateoas.CursoModelAssembler;
import com.cursos.Cursos.mapper.CursoMapper;
import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cursos", description = "Operaciones relacioandas con los cursos")
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
    @Operation(summary = "Obtener todos los cursos", description = "Obtiene una lista de todos los cursos creados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cursos encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CursoRespuestaDTO.class)))
            ),
            @ApiResponse(responseCode = "204", description = "No hay cursos registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Crear un nuevo curso ", description = "Crea un curso en la base de datos y devuelve el recurso con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Curso creado exitosamente",
                    content = @Content(schema = @Schema(implementation = CursoRespuestaDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CursoRespuestaDTO>> guardar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del curso a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CursoCrearDTO.class))
            )
            @RequestBody CursoCrearDTO dto)
    {
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
    @Operation(summary = "Buscar un  curso por ID", description = "Devuelve los datos del curso con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso encontrado",
                    content = @Content(schema = @Schema(implementation = CursoRespuestaDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CursoRespuestaDTO>> buscar(
            @Parameter(description = "ID del curso a buscar", required = true)
            @PathVariable("id") Long id) {
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
    @Operation(summary = "Actualizar un  curso", description = "Modifica los datos del curso indicado por ID y devuelve el recurso actualizado con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = CursoRespuestaDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<CursoRespuestaDTO>> actualizar(
            @Parameter(description = "ID del curso a actualizar", required = true)
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del curso a actualizar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CursoCrearDTO.class))
            )
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
    @Operation(summary = "Eliminar un  curso", description = "Elimina un curso existente de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del curso a eliminar", required = true)
            @PathVariable("id") Long id) {
        try {
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
