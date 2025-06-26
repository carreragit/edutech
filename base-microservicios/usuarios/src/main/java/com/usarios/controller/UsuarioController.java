package com.usarios.controller;

import com.usarios.dto.UsuarioCrearDTO;
import com.usarios.dto.UsuarioRespuestaDTO;
import com.usarios.mapper.UsuarioMapper;
import com.usarios.model.Usuario;
import com.usarios.service.UsuarioService;
import com.usarios.hateoas.UsuarioAssembler;

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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper mapper;

    @Autowired
    private UsuarioAssembler assembler;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios creados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioRespuestaDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<UsuarioRespuestaDTO>>> listar() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<UsuarioRespuestaDTO>> recursos = usuarios.stream()
                .map(mapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(recursos,
                        linkTo(methodOn(UsuarioController.class).listar()).withSelfRel())
        );
    }

    @PostMapping
    @Operation(summary = "Crear un usuario", description = "Permite crear un usuario y añadirlo a la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioRespuestaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud errónea (datos no válidos)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<UsuarioRespuestaDTO>> guardar(@RequestBody UsuarioCrearDTO dto) {
        Usuario nuevo = mapper.toEntity(dto);
        Usuario guardado = usuarioService.save(nuevo);
        UsuarioRespuestaDTO respuesta = mapper.toDTO(guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(respuesta));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario específico", description = "Permite obtener los datos de un usuario de la base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioRespuestaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID proporcionado inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<UsuarioRespuestaDTO>> buscar(
            @Parameter(description = "ID del usuario a buscar", required = true)
            @PathVariable("id") Long id) {
        try {
            Usuario usuario = usuarioService.findById(id);
            UsuarioRespuestaDTO dto = mapper.toDTO(usuario);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar un usuario específico", description = "Permite editar los datos de un usuario de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = UsuarioRespuestaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (ID o datos inválidos)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<UsuarioRespuestaDTO>> actualizar(
            @Parameter(description = "ID del usuario que se desea actualizar", required = true)
            @PathVariable("id") Long id,
            @RequestBody UsuarioCrearDTO dto) {
        try {
            Usuario existente = usuarioService.findById(id);
            mapper.updateEntity(existente, dto);
            Usuario actualizado = usuarioService.save(existente);
            UsuarioRespuestaDTO respuesta = mapper.toDTO(actualizado);
            return ResponseEntity.ok(assembler.toModel(respuesta));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario específico", description = "Permite eliminar un usuario de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable("id") Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
