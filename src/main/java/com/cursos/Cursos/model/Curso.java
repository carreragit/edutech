package com.cursos.Cursos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Curso
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String profesor;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String formato;


}
