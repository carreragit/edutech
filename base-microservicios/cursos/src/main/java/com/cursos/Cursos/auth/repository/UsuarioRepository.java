package com.cursos.Cursos.auth.repository;


import com.cursos.Cursos.auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional <Usuario> findByNombreUsuario(String nombreUsuario);


}
