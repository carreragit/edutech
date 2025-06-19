package com.usarios.mapper;

import com.usarios.dto.UsuarioCrearDTO;
import com.usarios.dto.UsuarioRespuestaDTO;
import com.usarios.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioRespuestaDTO toDTO(Usuario usuario) {
        UsuarioRespuestaDTO dto = new UsuarioRespuestaDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        return dto;
    }

    public Usuario toEntity(UsuarioCrearDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setContrasenia(dto.getContrasenia());
        return usuario;
    }

    public void updateEntity(Usuario usuario, UsuarioCrearDTO dto) {
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setContrasenia(dto.getContrasenia());
    }
} /* Si tu mapper tiene un metodo updateEntity(entidad, dto), simúlalo:
doAnswer(invocation -> {
Usuario u = invocation.getArgument(0);
UsuarioCrearDTO dto = invocation.getArgument(1);
        u.setNombreUsuario(dto.getNombreUsuario());
        u.setContrasenia(dto.getContrasenia());
        return null;
        }).when(usuarioMapper).updateEntity(existente, dtoEntrada);
*/