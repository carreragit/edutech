package com.auth.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // tabla en la base de datos
@Table (name = "usuario")
@Data // getters and setters
@NoArgsConstructor
@AllArgsConstructor


public class Usuario {

    @Id // clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id autoincremental
    private Long id;

    @Column( name = "nombre_usuario",unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "contrasenia",nullable = false)
    private String contrasenia;
}
