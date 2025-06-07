package com.cursos.Cursos.soporte.controller;

import com.cursos.Cursos.model.Curso;
import com.cursos.Cursos.soporte.model.Incidencia;
import com.cursos.Cursos.soporte.service.IncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController
{
    @Autowired
    private IncidenciaService incidenciaService;

    // LISTAR  las incidencias con el metodo get
    @GetMapping
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
    public ResponseEntity<Incidencia> guardar (@RequestBody Incidencia incidencia)
    {
        Incidencia inueva = incidenciaService.save(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(inueva);
    }

    // GET /api/incidencias/{id} - Buscar  incidencia por ID
    @GetMapping("/{id}")
    public ResponseEntity<Incidencia> buscar (@PathVariable Long id)
    {
        try
        {
            Incidencia incidencia = incidenciaService.findById(id);
            return ResponseEntity.ok(incidencia);
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /api/incidencias/{id} - EDITAR o ACTUALIZAR incidencia existente
    @PutMapping("/{id}")
    public ResponseEntity<Incidencia> actualizar (@PathVariable Long id, @RequestBody Incidencia incidencia)
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
    public ResponseEntity<?> eliminar (@PathVariable Long id)
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