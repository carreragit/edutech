package com.soporte1.controller;

import com.soporte1.dto.IncidenciaCrearDTO;
import com.soporte1.dto.IncidenciaRespuestaDTO;
import com.soporte1.mapper.IncidenciaMapper;
import com.soporte1.model.Incidencia;
import com.soporte1.service.IncidenciaService;
import com.soporte1.hateoas.IncidenciaModelAssembler; // IMPORTACIÓN DEL ASSEMBLER HATEOAS

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // Para linkTo y methodOn

@RestController
@RequestMapping("/api/incidencias")
@Tag(name = "Incidencias", description = "Operaciones relacionadas con las incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private IncidenciaMapper mapper;

    @Autowired
    private IncidenciaModelAssembler assembler; //  assembler para HATEOAS

    @GetMapping
    @Operation(summary = "Listar incidencias", description = "Obtiene la lista de todas las incidencias registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = IncidenciaRespuestaDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No hay incidencias registradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<IncidenciaRespuestaDTO>>> listar() { // NUEVO TIPO HATEOAS
        List<Incidencia> incidencias = incidenciaService.findAll();
        if (incidencias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<IncidenciaRespuestaDTO>> recursos = incidencias.stream()
                .map(mapper::toDTO)
                .map(assembler::toModel) // Convertimos DTO a EntityModel con enlaces
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(recursos, // Devolvemos la colección con enlace self
                        linkTo(methodOn(IncidenciaController.class).listar()).withSelfRel())
        );
    }

    @PostMapping
    @Operation(summary = "Crear incidencia", description = "Crea y guarda una nueva incidencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Incidencia creada exitosamente",
                    content = @Content(schema = @Schema(implementation = IncidenciaRespuestaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<IncidenciaRespuestaDTO>> guardar(@RequestBody IncidenciaCrearDTO dto) { // NUEVO TIPO
        Incidencia nueva = mapper.toEntity(dto);
        Incidencia guardada = incidenciaService.save(nueva);
        IncidenciaRespuestaDTO dtoRespuesta = mapper.toDTO(guardada);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(dtoRespuesta)); // Devuelve HATEOAS
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar incidencia", description = "Busca una incidencia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencia encontrada",
                    content = @Content(schema = @Schema(implementation = IncidenciaRespuestaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<IncidenciaRespuestaDTO>> buscar(
            @Parameter(description = "ID de la incidencia", required = true)
            @PathVariable("id") Long id) {
        try {
            Incidencia incidencia = incidenciaService.findById(id);
            IncidenciaRespuestaDTO dto = mapper.toDTO(incidencia);
            return ResponseEntity.ok(assembler.toModel(dto)); // Devuelve EntityModel con enlaces
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar incidencia", description = "Actualiza una incidencia existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencia actualizada",
                    content = @Content(schema = @Schema(implementation = IncidenciaRespuestaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<IncidenciaRespuestaDTO>> actualizar(
            @Parameter(description = "ID de la incidencia a actualizar", required = true)
            @PathVariable("id") Long id,
            @RequestBody IncidenciaCrearDTO dto) {
        try {
            Incidencia existente = incidenciaService.findById(id);
            mapper.updateEntity(existente, dto);
            Incidencia actualizada = incidenciaService.save(existente);
            IncidenciaRespuestaDTO respuesta = mapper.toDTO(actualizada);
            return ResponseEntity.ok(assembler.toModel(respuesta)); // Devuelve actualizado con enlaces
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar incidencia", description = "Elimina una incidencia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Incidencia eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la incidencia a eliminar", required = true)
            @PathVariable("id") Long id) {
        try {
            incidenciaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
