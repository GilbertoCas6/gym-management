package com.itla.gym.gym_management.servicio;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    // Método para autenticar usuario
    public Usuario autenticar(String email, String password) {
        Usuario usuario = usuarioRepositorio.findByEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    // Método para registrar nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        // TODO: Aquí deberías encriptar la contraseña con BCrypt
        // Por ahora se guarda en texto plano para el demo
        usuario.setActivo(true);
        if (usuario.getRol() == null) {
            usuario.setRol(Usuario.Rol.SOCIO);
        }
        return usuarioRepositorio.save(usuario);
    }

    // Verificar si existe un email
    public boolean existeEmail(String email) {
        return usuarioRepositorio.findByEmail(email) != null;
    }
}