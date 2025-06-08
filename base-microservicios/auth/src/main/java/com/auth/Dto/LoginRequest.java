package com.auth.Dto;


//Es una clase simple que usamos para recibir o enviar datos entre el cliente
//(por ejemplo, Postman o una app frontend) y tu backend Spring Boot.

import lombok.Data;

@Data
public class LoginRequest {
    private String nombreUsuario;
    private String contrasenia;

}
//Tomará el cuerpo del JSON.
//Lo convertirá en una instancia de LoginRequest.
//Te dará acceso a sus campos (nombreUsuario, contrasenia) en el controlador o servicio.