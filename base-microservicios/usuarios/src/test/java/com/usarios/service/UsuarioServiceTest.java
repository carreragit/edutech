package com.usarios.service;

import com.usarios.model.Usuario;
import com.usarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import net.datafaker.Faker;

import java.util.List;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private final Faker faker = new Faker();

    @Test
    void findAll_retornaListaDeUsuarios() {
        //  2 usuarios datos ficticios
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombreUsuario(faker.name().fullName());

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombreUsuario(faker.name().fullName());

        //se devuleve la lista de usuarios
        List<Usuario> usuariosFicticios = Arrays.asList(usuario1, usuario2);

        // simula el comportamiento del repositorio
        Mockito.when(usuarioRepository.findAll()).thenReturn(usuariosFicticios);

        // Llama al metodo del service.
        List<Usuario> resultado = usuarioService.findAll();

        // verifica el resultado
        assertEquals(2, resultado.size());
        assertEquals(usuario1.getNombreUsuario(), resultado.get(0).getNombreUsuario());
        assertEquals(usuario2.getNombreUsuario(), resultado.get(1).getNombreUsuario());

        // se verifica que el repositorio fue consultado exactamente una vez.
        verify(usuarioRepository, times(1)).findAll();

    }

    @Test
    void findById_usuarioExiste_retornaUsuario() {
        //datos ficticios
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombreUsuario(faker.name().fullName());

        // mock del repositorio, simula que el repositorio encuentra el usuario por ID
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        //ejecutar metodo del service
        Usuario resultado = usuarioService.findById(id);

        //verificar resultado
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(usuario.getNombreUsuario(), resultado.getNombreUsuario());

        // verificar interaccion
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void findById_usuarioNotExiste_lanzarExcepcion() {
        Long id = 100L;

        //simula que el usuario no existe
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        //
        assertThrows(NoSuchElementException.class, () -> usuarioService.findById(id));

        //comprueba que el metodo del repositorio fue llamado una vez.
        verify(usuarioRepository, times(1)).findById(id);
    }
    @Test
    void save_GuardarUsuario_retornaUsuario() {
        // crear usuario ficticio para guardar
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario(faker.name().fullName());

        // configurar el mock, al llamar a save devuelva el mismo usuario
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.save(usuario);

        // verificar que el usuario devuelto es correcto y no es null
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());

        // repositorio due llamado una vez
        verify(usuarioRepository, times(1)).save(usuario);


    }

    @Test
    void delete_eliminarUsuario_llamaAlRepositorio() {
        Long id = 1L;

        // Configura el mock para que "deleteById" no haga nada (es void)
        doNothing().when(usuarioRepository).deleteById(id);

        //llama al metodo del service
        usuarioService.delete(id);

        // verifica que el repositorio fue llamado una vez
        verify(usuarioRepository, times(1)).deleteById(id);


    }
}