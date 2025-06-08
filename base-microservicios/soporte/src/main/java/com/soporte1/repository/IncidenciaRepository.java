package com.soporte1.repository;

import com.soporte1.model.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long>
{
    // No necesito métodos adicionales para el CRUD básico.
    // Spring Data JPA provee findAll(), save(), deleteById(), etc.
}
