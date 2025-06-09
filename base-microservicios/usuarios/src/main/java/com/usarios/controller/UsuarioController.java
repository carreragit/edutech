package com.usarios.controller;

import com.usarios.model.Usuario;
import com.usarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UsuarioController
{
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
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
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario)
    {
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id)
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
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario)
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
    public ResponseEntity<?> eliminar(@PathVariable Long id)
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
