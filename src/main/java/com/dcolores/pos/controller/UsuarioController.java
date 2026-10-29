package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Usuario;
import com.dcolores.pos.service.UsuarioService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @InitBinder("nuevoUsuario")
    public void configurarFormulario(WebDataBinder binder) {
        binder.setAllowedFields("id_usuario", "username", "nombre", "apellido",
                "correo", "telefono", "password", "rol");
    }

    @GetMapping
    public String mostrarUsuarios(Model model) {
        cargarVista(model, new Usuario());
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("nuevoUsuario") Usuario usuario,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "No se pudo guardar el usuario. Verifica los datos ingresados.");
            return "redirect:/usuarios";
        }

        try {
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("exito", "Usuario guardado correctamente.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo guardar el usuario. Verifica que el nombre de usuario no esté repetido y que los datos sean válidos.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario;
        try {
            usuario = usuarioService.buscarPorId(id);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/usuarios";
        }

        Usuario formulario = new Usuario();
        formulario.setId_usuario(usuario.getId_usuario());
        formulario.setUsername(usuario.getUsername());
        formulario.setNombre(usuario.getNombre());
        formulario.setApellido(usuario.getApellido());
        formulario.setCorreo(usuario.getCorreo());
        formulario.setTelefono(usuario.getTelefono());
        formulario.setRol(usuario.getRol());
        formulario.setActivo(usuario.getActivo());

        cargarVista(model, formulario);
        return "usuarios";
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarEstado(id);
            redirectAttributes.addFlashAttribute("exito", "Estado del usuario actualizado correctamente.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/usuarios";
    }

    private void cargarVista(Model model, Usuario formulario) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("nuevoUsuario", formulario);
        model.addAttribute("usuarios", usuarioService.listarTodos());
    }
}
