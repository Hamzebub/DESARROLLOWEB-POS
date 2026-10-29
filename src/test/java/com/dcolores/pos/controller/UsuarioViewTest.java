package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Usuario;
import com.dcolores.pos.repository.VentaRepository;
import com.dcolores.pos.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UsuarioController.class, VentaController.class})
class UsuarioViewTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private VentaRepository ventaRepository;

    @Test
    void registroVacioRenderizaElLayoutYRequiereContrasena() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        String html = renderizar("/usuarios");

        assertTrue(html.contains("Usuarios - D&#39;Colores POS"));
        assertTrue(html.contains("Sra. Gloria"));
        assertTrue(html.contains("Registrar usuario"));
        assertTrue(html.contains("No hay usuarios registrados."));
        assertTrue(html.contains("action=\"/usuarios/guardar\""));
        assertTrue(input(html, "password").contains("required"));
        assertFalse(input(html, "password").contains("value="));
        assertTrue(input(html, "telefono").contains("pattern=\"[0-9]{9}\""));
        assertTrue(html.contains("value=\"ADMIN\""));
        assertTrue(html.contains("value=\"CAJERO\""));
        assertTrue(html.contains("value=\"GERENTE\""));
        assertTrue(Pattern.compile("<option(?=[^>]*value=\"\")(?=[^>]*selected)[^>]*>").matcher(html).find());
        assertFalse(html.contains(">Cancelar</a>"));
        guardarVistaPrevia("vacio.html", html);
    }

    @Test
    void edicionCargaLosCamposYPermiteConservarLaContrasena() throws Exception {
        Usuario existente = usuario(7L, "GERENTE", false);
        when(usuarioService.buscarPorId(7L)).thenReturn(existente);
        when(usuarioService.listarTodos()).thenReturn(List.of(existente));

        String html = renderizar("/usuarios/editar/7");

        assertTrue(html.contains("Editar usuario"));
        assertTrue(input(html, "id_usuario").contains("value=\"7\""));
        assertTrue(input(html, "nombre").contains("value=\"Juan\""));
        assertTrue(input(html, "username").contains("value=\"jperez7\""));
        assertFalse(input(html, "password").contains("required"));
        assertFalse(input(html, "password").contains("value="));
        assertTrue(html.contains("Deja este campo vacío para conservar la contraseña actual."));
        assertTrue(html.contains(">Actualizar</button>"));
        assertTrue(html.contains(">Cancelar</a>"));
        assertTrue(Pattern.compile("<option(?=[^>]*value=\"GERENTE\")(?=[^>]*selected)[^>]*>").matcher(html).find());
        assertFalse(html.contains("credencial-no-visible"));
        assertFalse(html.contains("hash-no-visible"));
        guardarVistaPrevia("edicion.html", html);
    }

    @Test
    void listadoRenderizaRolesEstadosYFormulariosPost() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(
                usuario(1L, "ADMIN", true), usuario(2L, "GERENTE", false), usuario(3L, "CAJERO", true)));

        String html = renderizar("/usuarios");

        assertTrue(fila(html, 1).contains("bg-primary"));
        assertTrue(fila(html, 2).contains("bg-info text-dark"));
        assertTrue(fila(html, 3).contains("bg-secondary"));
        assertTrue(fila(html, 1).contains("bg-success"));
        assertTrue(fila(html, 2).contains("bg-danger"));
        assertTrue(fila(html, 1).contains(">Desactivar</button>"));
        assertTrue(fila(html, 2).contains(">Activar</button>"));
        assertTrue(fila(html, 1).contains("href=\"/usuarios/editar/1\""));
        assertTrue(fila(html, 1).contains("action=\"/usuarios/estado/1\" method=\"post\""));
        assertTrue(html.contains("Juan Pérez"));
        assertFalse(html.contains("No hay usuarios registrados."));
        assertFalse(html.contains("credencial-no-visible"));
        assertFalse(html.contains("hash-no-visible"));
        guardarVistaPrevia("registro.html", html);
    }

    @Test
    void mensajeDeExitoSeMuestraEnLaVista() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        String html = mockMvc.perform(get("/usuarios").flashAttr("exito", "Usuario guardado correctamente."))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertTrue(html.contains("alert alert-success"));
        assertTrue(html.contains("Usuario guardado correctamente."));
        assertFalse(html.contains("alert alert-danger"));
    }

    @Test
    void erroresYDatosDelUsuarioSeEscapanComoTexto() throws Exception {
        Usuario existente = usuario(1L, "ADMIN", true);
        existente.setUsername("<script>alert('usuario')</script>");
        when(usuarioService.listarTodos()).thenReturn(List.of(existente));

        String html = mockMvc.perform(get("/usuarios").flashAttr("error", "<script>alert('error')</script>"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertTrue(html.contains("alert alert-danger"));
        assertTrue(html.contains("&lt;script&gt;"));
        assertFalse(html.contains("<script>alert("));
        assertFalse(html.contains("alert alert-success"));
    }

    @Test
    void fusionConservaTodosLosEnlacesDelSidebarEnElOrdenAcordado() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        String html = renderizar("/usuarios");
        String nav = html.substring(html.indexOf("<nav "), html.indexOf("</nav>"));
        List<String> rutas = Pattern.compile("href=\"([^\"]+)\"").matcher(nav).results()
                .map(resultado -> resultado.group(1)).toList();

        assertEquals(List.of("/menu", "/usuarios", "/inventario", "/categorias", "/productos",
                "/ventas", "/mermas", "/listaventa"), rutas);
        assertTrue(enlaceActivo(html, "/usuarios"));
        assertFalse(enlaceActivo(html, "/listaventa"));
    }

    @Test
    void listaDeVentasConservaSuVistaYElResalteTrasLaFusion() throws Exception {
        when(ventaRepository.findAll()).thenReturn(List.of());

        String html = renderizar("/listaventa");

        assertTrue(html.contains("Lista General de Ventas"));
        assertTrue(enlaceActivo(html, "/listaventa"));
        assertFalse(enlaceActivo(html, "/usuarios"));
    }

    private boolean enlaceActivo(String html, String ruta) {
        return Pattern.compile("<a\\b(?=[^>]*href=\"" + Pattern.quote(ruta)
                + "\")(?=[^>]*class=\"[^\"]*\\bactive\\b)[^>]*>").matcher(html).find();
    }

    private String renderizar(String ruta) throws Exception {
        return mockMvc.perform(get(ruta)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private String input(String html, String id) {
        Matcher matcher = Pattern.compile("<input\\b(?=[^>]*\\bid=\"" + Pattern.quote(id) + "\")[^>]*>").matcher(html);
        assertTrue(matcher.find(), "No se encontró el campo " + id);
        return matcher.group();
    }

    private String fila(String html, long id) {
        Matcher matcher = Pattern.compile("<tr>\\s*<td[^>]*>" + id + "</td>.*?</tr>", Pattern.DOTALL).matcher(html);
        assertTrue(matcher.find(), "No se encontró la fila " + id);
        return matcher.group();
    }

    private void guardarVistaPrevia(String archivo, String html) throws Exception {
        String directorio = System.getProperty("usuarios.preview.dir");
        if (directorio != null) {
            Path destino = Path.of(directorio);
            Files.createDirectories(destino);
            Files.writeString(destino.resolve(archivo), html);
        }
    }

    private Usuario usuario(Long id, String rol, boolean activo) {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(id);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setUsername("jperez" + id);
        usuario.setCorreo("jperez" + id + "@dcolores.com");
        usuario.setTelefono("987654321");
        usuario.setRol(rol);
        usuario.setActivo(activo);
        usuario.setPassword("credencial-no-visible");
        usuario.setPassword_hash("hash-no-visible");
        return usuario;
    }
}
