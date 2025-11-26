package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioServicio.listarUsuarios();
    }

    @GetMapping("/rol/{rol}")
    public List<Usuario> listarPorRol(@PathVariable String rol) {
        return usuarioServicio.listarPorRol(Usuario.Rol.valueOf(rol.toUpperCase()));
    }

    @GetMapping("/entrenador/{idEntrenador}/clientes")
    public List<Usuario> listarClientesDeEntrenador(@PathVariable Long idEntrenador) {
        return usuarioServicio.listarClientesDeEntrenador(idEntrenador);
    }

    @GetMapping("/clientes/activos")
    public List<Usuario> listarClientesActivos() {
        return usuarioServicio.listarClientesActivos();
    }

    @GetMapping("/entrenadores/activos")
    public List<Usuario> listarEntrenadoresActivos() {
        return usuarioServicio.listarEntrenadoresActivos();
    }

    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario) {
        return usuarioServicio.guardarUsuario(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioServicio.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return usuarioServicio.guardarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
    }

    @GetMapping("/ping")
    public String ping() {
        return "Controlador de usuarios activo ✅";
    }
}