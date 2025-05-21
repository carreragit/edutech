package com.cursos.Cursos.controller;


import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.service.CursoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController
{

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar()
    {
        List<Curso> cursos = cursoService.findAll();
        if (cursos.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cursos);
    }

    @PostMapping
    public ResponseEntity<Curso> guardar (@RequestBody Curso curso)
    {
        Curso cnuevo = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cnuevo);
    }

    @GetMapping
    public ResponseEntity<Curso> buscar (@PathVariable Integer id)
    {
        try
        {
            Curso curso = cursoService.findById(id);
            return ResponseEntity.ok(curso);
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar (@PathVariable Integer id, @RequestBody Curso curso)
    {
        try
        {
            Curso curso1 = cursoService.findById(id);
            curso1.setId(id);
            curso1.setNombre(curso.getNombre());
            curso1.setProfesor(curso.getProfesor());
            curso1.setDescripcion(curso.getDescripcion());
            curso1.setFormato(curso.getFormato());

            cursoService.save(curso1);
            return ResponseEntity.ok(curso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id)
    {
        try
        {
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}