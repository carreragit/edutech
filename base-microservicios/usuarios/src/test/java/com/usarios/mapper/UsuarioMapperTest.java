package com.usarios.mapper;

import com.usarios.dto.UsuarioCrearDTO;
import com.usarios.dto.UsuarioRespuestaDTO;
import com.usarios.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Test
    void toDTO_debeMapearCamposCorrectamente() { // verifica que el metodo toDTO copia id y nombre correctamente.
        // se crea un objeto Usuario
        Usuario usuario = new Usuario();
        usuario.setId(7L);
        usuario.setNombreUsuario("Usuario 1");
        usuario.setContrasenia("123456");

        // convierte el usuario a un DTO de respuesta usando el metodo a testear.
        UsuarioRespuestaDTO dto = usuarioMapper.toDTO(usuario);

        // verifica que el id y nombre del DTO sean iguales a los del usuario original.
        assertEquals(usuario.getId(), dto.getId());
        assertEquals(usuario.getNombreUsuario(), dto.getNombreUsuario());

        }

    @Test
    void toEntity_debeMapearCamposCorrectamente(){ // verifica que el metodo toEntity copia nombre y contraseña correctamente del DTO a la entidad.
    //crear DTO
    UsuarioCrearDTO dto = new UsuarioCrearDTO();
    dto.setNombreUsuario("Usuario 1");
    dto.setContrasenia("123456");

    // convierte DTO en una entidad Usuario usando el metodo a testear
       Usuario usuario = usuarioMapper.toEntity(dto);

        //Verifica que el nombre y contraseña del usuario sean iguales a los del DTO.
        assertEquals(dto.getNombreUsuario(), usuario.getNombreUsuario());
        assertEquals(dto.getContrasenia(), usuario.getContrasenia());


    }

    @Test
    void updateEntity_debeActualizarLosCampos() { //verifica que el metodo updateEntity actualiza los campos de la entidad, sin cambiar el id.
    //crea un usuario con valores originales e id 1.
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNombreUsuario("original");
    usuario.setContrasenia("originalpassword");

    //crea un DTO con nuevos valores para nombre y contraseña.
    UsuarioCrearDTO dto = new UsuarioCrearDTO();
    dto.setNombreUsuario("nuevoNombreUsuario");
    dto.setContrasenia("nuevoContrasenia");

    //actualiza el usuario con los datos del DTO usando el metodo a testear.
    usuarioMapper.updateEntity(usuario, dto);

    //verifica que el usuario ahora tiene los nuevos datos del DTO.
    assertEquals("nuevoNombreUsuario", usuario.getNombreUsuario());
    assertEquals("nuevoContrasenia", usuario.getContrasenia());
    assertEquals(1L, usuario.getId()); // verifica que el id del usuario NO cambió (sigue siendo 1).
    }
}