package com.soporte1.service;

import com.soporte1.model.Incidencia;
import com.soporte1.repository.IncidenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional // Añade transacciones a nivel de clase
public class IncidenciaService
{
    @Autowired
    private IncidenciaRepository repository;

    public List<Incidencia> findAll() {
        return repository.findAll();
    }

    public Incidencia findById(Long id) {
        return repository.findById(id).get(); // Lanza excepción si no existe
    }

    public Incidencia save(Incidencia incidencia) {
        return repository.save(incidencia);
    }

    public void delete(Long id) {
        repository.deleteById(id); // No verifica existencia
    }
}