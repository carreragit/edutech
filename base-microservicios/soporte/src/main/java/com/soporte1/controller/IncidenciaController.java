package com.soporte1.controller;


import com.soporte1.SoporteApplication;
import com.soporte1.model.Incidencia;
import com.soporte1.service.IncidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
@Tag(name = "Insidencias", description = "Operaciones relacionadas con las incidencias")
public class IncidenciaController
{
    @Autowired
    private IncidenciaService incidenciaService;

    // LISTAR  las incidencias con el metodo get
    @GetMapping
    @Operation(summary = "Listar incidencias", description = "Obtienes las listas de incidencias")
    @ApiResponses(value = { // documento las respuestas http (que significan)
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de incidencias encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Incidencia.class)))
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No hay incidencias registrados"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<Incidencia>> listar()
    {
        List<Incidencia> incidencias = incidenciaService.findAll();
        if (incidencias.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(incidencias);
    }

    // POST /api/incidencias - Crear nueva incidencia
    @PostMapping
    @Operation(summary = "Crear incidencia", description = "Crea una incidencias y la guarda")
    @ApiResponses(value = { // documento las respuestas http (que significan)
            @ApiResponse(
                    responseCode = "201",
                    description = "Incidencia creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Incidencia.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud erronea (datos no válidos)"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<Incidencia> guardar (@RequestBody Incidencia incidencia)
    {
        Incidencia inueva = incidenciaService.save(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(inueva);
    }

    // GET /api/incidencias/{id} - Buscar  incidencia por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar incidencia", description = "Busca una incidencia en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Incidencia encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Incidencia.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Incidencia no encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID proporcionado inválido"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<Incidencia> buscar (
            @Parameter(description = "ID de la incidencia a buscar", required = true)
            @PathVariable("id") Long id)
    {
        try
        {
            Incidencia incidencia = incidenciaService.findById(id);
            return ResponseEntity.ok(incidencia);
        }
        catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /api/incidencias/{id} - EDITAR o ACTUALIZAR incidencia existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar incidencia", description = "Actualiza una incidencia en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Incidencia actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Incidencia.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud incorrecta (ID o datos inválidos)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Incidencia no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })    public ResponseEntity<Incidencia> actualizar (
            @Parameter(description = "ID del incidencia que se desea actualizar", required = true)
            @PathVariable("id") Long id, @RequestBody Incidencia incidencia)
    {
        try
        {
            Incidencia incidencia1 = incidenciaService.findById(id);
            incidencia1.setId(id);
            incidencia1.setTitulo(incidencia.getTitulo());
            incidencia1.setDescripcion(incidencia.getDescripcion());
            incidencia1.setEstado(incidencia.getEstado());
            incidencia1.setUsuarioId(incidencia.getUsuarioId());


            incidenciaService.save(incidencia1);
            return ResponseEntity.ok(incidencia);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/incidencias/{id} - Eliminar una incidencia
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminaer incidencia", description = "Elimina una incidencia de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Incidencia eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Incidencia no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<?> eliminar (
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable("id") Long id)
    {
        try
        {
            incidenciaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}