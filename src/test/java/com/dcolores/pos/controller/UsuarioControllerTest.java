package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Usuario;
import com.dcolores.pos.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        // Estas pruebas aíslan las rutas; UsuarioViewTest verifica la vista Thymeleaf real.
        mockMvc = standaloneSetup(new UsuarioController(usuarioService))
                .setViewResolvers(new InternalResourceViewResolver("/test-views/", ".html"))
                .build();
    }

    @Test
    void listarCargaEncabezadoFormularioYUsuarios() throws Exception {
        List<Usuario> usuarios = List.of(usuarioExistente());
        when(usuarioService.listarTodos()).thenReturn(usuarios);

        MvcResult resultado = mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios"))
                .andExpect(model().attribute("usuario", "Sra. Gloria"))
                .andExpect(model().attribute("usuarios", usuarios))
                .andReturn();

        Usuario formulario = (Usuario) resultado.getModelAndView().getModel().get("nuevoUsuario");
        assertNotNull(formulario);
        assertNull(formulario.getId_usuario());
        assertNull(formulario.getPassword());
        assertNull(formulario.getPassword_hash());
    }

    @Test
    void guardarEnlazaLosDatosYRedirigeConExito() throws Exception {
        mockMvc.perform(post("/usuarios/guardar")
                        .param("username", "jperez")
                        .param("nombre", "Juan")
                        .param("apellido", "Pérez")
                        .param("correo", "jperez@dcolores.com")
                        .param("telefono", "987654321")
                        .param("password", "clave-nueva")
                        .param("rol", "CAJERO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("exito", "Usuario guardado correctamente."))
                .andExpect(flash().attributeCount(1));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioService).guardar(captor.capture());
        Usuario recibido = captor.getValue();
        assertNull(recibido.getId_usuario());
        assertEquals("jperez", recibido.getUsername());
        assertEquals("Juan", recibido.getNombre());
        assertEquals("Pérez", recibido.getApellido());
        assertEquals("jperez@dcolores.com", recibido.getCorreo());
        assertEquals("987654321", recibido.getTelefono());
        assertEquals("clave-nueva", recibido.getPassword());
        assertEquals("CAJERO", recibido.getRol());
    }

    @Test
    void guardarEdicionConservaElIdYExcluyeCamposInternosDelFormulario() throws Exception {
        mockMvc.perform(post("/usuarios/guardar")
                        .param("id_usuario", "7")
                        .param("username", "jperez")
                        .param("password", "")
                        .param("activo", "true")
                        .param("password_hash", "valor-no-permitido"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attributeExists("exito"));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioService).guardar(captor.capture());
        Usuario recibido = captor.getValue();
        assertEquals(7L, recibido.getId_usuario());
        assertEquals("", recibido.getPassword());
        assertNull(recibido.getPassword_hash());
        assertNull(recibido.getActivo());
    }

    @Test
    void guardarDuplicadoMuestraErrorSinExponerLaExcepcionDeBaseDeDatos() throws Exception {
        when(usuarioService.guardar(any(Usuario.class)))
                .thenThrow(new DataIntegrityViolationException("Detalle interno SQL"));

        mockMvc.perform(post("/usuarios/guardar").param("username", "repetido"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("error",
                        "No se pudo guardar el usuario. Verifica que el nombre de usuario no esté repetido y que los datos sean válidos."))
                .andExpect(flash().attributeCount(1));
    }

    @Test
    void guardarConErrorDeNegocioLoComunicaAlUsuario() throws Exception {
        String mensaje = "La contraseña es obligatoria para un usuario nuevo.";
        when(usuarioService.guardar(any(Usuario.class)))
                .thenThrow(new IllegalArgumentException(mensaje));

        mockMvc.perform(post("/usuarios/guardar").param("username", "jperez"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("error", mensaje))
                .andExpect(flash().attributeCount(1));
    }

    @Test
    void guardarConIdInvalidoNoInvocaElServicio() throws Exception {
        mockMvc.perform(post("/usuarios/guardar").param("id_usuario", "no-es-un-numero"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("error", "No se pudo guardar el usuario. Verifica los datos ingresados."));

        verifyNoInteractions(usuarioService);
    }

    @Test
    void editarCargaLosDatosSinCopiarNiBorrarLasCredencialesExistentes() throws Exception {
        Usuario existente = usuarioExistente();
        List<Usuario> usuarios = List.of(existente);
        when(usuarioService.buscarPorId(7L)).thenReturn(existente);
        when(usuarioService.listarTodos()).thenReturn(usuarios);

        MvcResult resultado = mockMvc.perform(get("/usuarios/editar/7"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios"))
                .andExpect(model().attribute("usuario", "Sra. Gloria"))
                .andExpect(model().attribute("usuarios", usuarios))
                .andReturn();

        Usuario formulario = (Usuario) resultado.getModelAndView().getModel().get("nuevoUsuario");
        assertNotSame(existente, formulario);
        assertEquals(7L, formulario.getId_usuario());
        assertEquals(existente.getUsername(), formulario.getUsername());
        assertEquals(existente.getNombre(), formulario.getNombre());
        assertEquals(existente.getApellido(), formulario.getApellido());
        assertEquals(existente.getCorreo(), formulario.getCorreo());
        assertEquals(existente.getTelefono(), formulario.getTelefono());
        assertEquals(existente.getRol(), formulario.getRol());
        assertEquals(existente.getActivo(), formulario.getActivo());
        assertNull(formulario.getPassword());
        assertNull(formulario.getPassword_hash());
        assertEquals("clave-actual", existente.getPassword());
        assertEquals("hash-actual", existente.getPassword_hash());
    }

    @Test
    void editarIdInexistenteRedirigeConError() throws Exception {
        when(usuarioService.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado: 99"));

        mockMvc.perform(get("/usuarios/editar/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("error", "Usuario no encontrado: 99"));

        verify(usuarioService, never()).listarTodos();
    }

    @Test
    void cambiarEstadoRedirigeConMensajeDeExito() throws Exception {
        mockMvc.perform(post("/usuarios/estado/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("exito", "Estado del usuario actualizado correctamente."));

        verify(usuarioService).cambiarEstado(7L);
    }

    @Test
    void cambiarEstadoDeIdInexistenteRedirigeConError() throws Exception {
        when(usuarioService.cambiarEstado(99L))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado: 99"));

        mockMvc.perform(post("/usuarios/estado/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"))
                .andExpect(flash().attribute("error", "Usuario no encontrado: 99"));
    }

    @Test
    void cambiarEstadoRechazaGetSinInvocarElServicio() throws Exception {
        mockMvc.perform(get("/usuarios/estado/7"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(usuarioService);
    }

    private Usuario usuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(7L);
        usuario.setUsername("jperez");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreo("jperez@dcolores.com");
        usuario.setTelefono("987654321");
        usuario.setRol("CAJERO");
        usuario.setActivo(false);
        usuario.setPassword("clave-actual");
        usuario.setPassword_hash("hash-actual");
        return usuario;
    }
}
