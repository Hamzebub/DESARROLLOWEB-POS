package com.dcolores.pos.service;

import com.dcolores.pos.entity.Usuario;
import com.dcolores.pos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
    }

    @Transactional
    public Usuario guardar(Usuario usuario) {
        String password = usuario.getPassword();
        boolean tieneContrasena = password != null && !password.isBlank();
        Usuario usuarioAGuardar;

        if (usuario.getId_usuario() == null) {
            if (!tieneContrasena) {
                throw new IllegalArgumentException("La contraseña es obligatoria para un usuario nuevo.");
            }
            usuarioAGuardar = usuario;
            usuarioAGuardar.setActivo(true);
        } else {
            usuarioAGuardar = buscarPorId(usuario.getId_usuario());
            usuarioAGuardar.setUsername(usuario.getUsername());
            usuarioAGuardar.setNombre(usuario.getNombre());
            usuarioAGuardar.setApellido(usuario.getApellido());
            usuarioAGuardar.setCorreo(usuario.getCorreo());
            usuarioAGuardar.setTelefono(usuario.getTelefono());
            usuarioAGuardar.setRol(usuario.getRol());
        }

        if (tieneContrasena) {
            usuarioAGuardar.setPassword(password);
            usuarioAGuardar.setPassword_hash(password);
        }

        return usuarioRepository.save(usuarioAGuardar);
    }

    @Transactional
    public Usuario cambiarEstado(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(!Boolean.TRUE.equals(usuario.getActivo()));
        return usuarioRepository.save(usuario);
    }
}
