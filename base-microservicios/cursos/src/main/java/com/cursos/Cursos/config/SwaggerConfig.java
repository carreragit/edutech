package com.cursos.Cursos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.Line;

@Configuration
public class SwaggerConfig
{
    @Bean
    public OpenAPI customOpenApi ()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Api Microservicio de Cursos")
                        .version("1.0")
                        .description("Documentación de la API para el microservicio de Cursos")
                );
    }
}