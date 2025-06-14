package com.usarios.controller;

import com.usarios.model.Usuario;
import com.usarios.service.UsuarioService;
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
@RequestMapping("/auth")
@Tag(name = "Usuarios", description = "Operaciones relacioandas con los usuarios")
public class UsuarioController
{
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios creados")
    @ApiResponses(value = { // documento las respuestas http (que significan)
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No hay usuarios registrados"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<Usuario>> listar()
    {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    @Operation(summary = "Crear un usuario", description = "Permite crear un usuario y añadirlo a la base de datos")
    @ApiResponses(value = { // documento las respuestas http (que significan)
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))
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
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario)
    {
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario específico", description = "Permite obtener los datos de un usuario de la base")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
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
    public ResponseEntity<Usuario> buscar(
            @Parameter(description = "ID del usuario a buscar", required = true)
            @PathVariable("id") Integer id)
    {
        try
        {
            Usuario usuario = usuarioService.findById(id);
            return ResponseEntity.ok(usuario);
        }
        catch ( Exception e )
        {
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar un usuario específico", description = "Permite editar los datos de un usuario de la base de datos")
    public ResponseEntity<Usuario> actualizar(@PathVariable("id") Integer id, @RequestBody Usuario usuario)
    {
        try
        {
            Usuario usuario1 = usuarioService.findById(id);
            usuario1.setId(id);
            usuario1.setNombreUsuario(usuario.getNombreUsuario());
            usuario1.setContrasenia(usuario.getContrasenia());
            usuarioService.save(usuario1);
            return ResponseEntity.ok(usuario);

        }
        catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id)
    {
        try
        {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch ( Exception e)
        {
            return  ResponseEntity.notFound().build();
        }
    }
}
