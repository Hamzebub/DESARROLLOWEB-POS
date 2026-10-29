package com.dcolores.pos.service;

import com.dcolores.pos.entity.Usuario;
import com.dcolores.pos.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarUsuarioLoActivaYSincronizaAmbosCamposDeContrasena() {
        Usuario nuevo = datosFormulario();
        nuevo.setPassword("clave-nueva");
        nuevo.setPassword_hash("valor-ignorado");
        nuevo.setActivo(false);
        permitirGuardar();

        Usuario guardado = usuarioService.guardar(nuevo);

        assertTrue(guardado.getActivo());
        assertEquals("clave-nueva", guardado.getPassword());
        assertEquals("clave-nueva", guardado.getPassword_hash());
        verify(usuarioRepository).save(nuevo);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void registrarSinContrasenaNoGuarda(String password) {
        Usuario nuevo = datosFormulario();
        nuevo.setPassword(password);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardar(nuevo));

        verifyNoInteractions(usuarioRepository);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void editarSinContrasenaConservaCredencialesYEstado(String password) {
        Usuario existente = usuarioExistente();
        Usuario formulario = datosFormulario();
        formulario.setId_usuario(7L);
        formulario.setPassword(password);
        formulario.setPassword_hash("valor-ignorado");
        formulario.setActivo(true);
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(existente));
        permitirGuardar();

        Usuario guardado = usuarioService.guardar(formulario);

        assertSame(existente, guardado);
        assertEquals("clave-actual", guardado.getPassword());
        assertEquals("hash-actual", guardado.getPassword_hash());
        assertFalse(guardado.getActivo());
        assertEquals(formulario.getUsername(), guardado.getUsername());
        assertEquals(formulario.getNombre(), guardado.getNombre());
        assertEquals(formulario.getApellido(), guardado.getApellido());
        assertEquals(formulario.getCorreo(), guardado.getCorreo());
        assertEquals(formulario.getTelefono(), guardado.getTelefono());
        assertEquals(formulario.getRol(), guardado.getRol());
        verify(usuarioRepository).save(existente);
    }

    @Test
    void editarConContrasenaNuevaSincronizaCredencialesYConservaEstado() {
        Usuario existente = usuarioExistente();
        Usuario formulario = datosFormulario();
        formulario.setId_usuario(7L);
        formulario.setPassword("clave-actualizada");
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(existente));
        permitirGuardar();

        Usuario guardado = usuarioService.guardar(formulario);

        assertEquals("clave-actualizada", guardado.getPassword());
        assertEquals("clave-actualizada", guardado.getPassword_hash());
        assertFalse(guardado.getActivo());
        verify(usuarioRepository).save(existente);
    }

    @Test
    void editarUnIdInexistenteNoCreaOtroUsuario() {
        Usuario formulario = datosFormulario();
        formulario.setId_usuario(99L);
        formulario.setPassword("clave-nueva");
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardar(formulario));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @ParameterizedTest
    @CsvSource({"true, false", "false, true", ", true"})
    void cambiarEstadoAlternaSinEliminarNiModificarCredenciales(Boolean estado, boolean esperado) {
        Usuario existente = usuarioExistente();
        existente.setActivo(estado);
        when(usuarioRepository.findById(7L)).thenReturn(Optional.of(existente));
        permitirGuardar();

        Usuario guardado = usuarioService.cambiarEstado(7L);

        assertSame(existente, guardado);
        assertEquals(esperado, guardado.getActivo());
        assertEquals(7L, guardado.getId_usuario());
        assertEquals("clave-actual", guardado.getPassword());
        assertEquals("hash-actual", guardado.getPassword_hash());
        verify(usuarioRepository).findById(7L);
        verify(usuarioRepository).save(existente);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void cambiarEstadoDeUnIdInexistenteNoGuarda() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> usuarioService.cambiarEstado(99L));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    private void permitirGuardar() {
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private Usuario datosFormulario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("jperez");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreo("jperez@dcolores.com");
        usuario.setTelefono("987654321");
        usuario.setRol("CAJERO");
        return usuario;
    }

    private Usuario usuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(7L);
        usuario.setUsername("usuario-anterior");
        usuario.setNombre("Nombre anterior");
        usuario.setApellido("Apellido anterior");
        usuario.setCorreo("anterior@dcolores.com");
        usuario.setTelefono("999999999");
        usuario.setRol("GERENTE");
        usuario.setPassword("clave-actual");
        usuario.setPassword_hash("hash-actual");
        usuario.setActivo(false);
        return usuario;
    }
}
