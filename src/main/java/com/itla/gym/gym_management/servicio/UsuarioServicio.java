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

    public List<Usuario> listarPorRol(Usuario.Rol rol) {
        return usuarioRepositorio.findByRol(rol);
    }

    public List<Usuario> listarClientesDeEntrenador(Long idEntrenador) {
        return usuarioRepositorio.findByIdEntrenador(idEntrenador);
    }

    public List<Usuario> listarClientesActivos() {
        return usuarioRepositorio.findByRolAndActivo(Usuario.Rol.CLIENTE, true);
    }

    public List<Usuario> listarEntrenadoresActivos() {
        return usuarioRepositorio.findByRolAndActivo(Usuario.Rol.ENTRENADOR, true);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email);
    }

    public boolean existeEmail(String email) {
        return usuarioRepositorio.findByEmail(email) != null;
    }

    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }
}