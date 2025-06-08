package com.soporte1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "incidencias")
@NoArgsConstructor
@AllArgsConstructor

public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String estado; // "ABIERTA", "EN_PROGRESO", "CERRADA"

    @Column(name = "usuario_id")  // Relación con el microservicio de usuarios (solo ID)
    private Long usuarioId;       // No es una FK real, es una referencia lógica
}
