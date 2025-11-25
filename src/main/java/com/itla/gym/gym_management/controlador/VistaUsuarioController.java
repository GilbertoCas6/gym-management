package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class VistaUsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String listarUsuarios(Model model, HttpSession session) {
        // Verificar que sea admin
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("title", "Gestión de Usuarios");
        model.addAttribute("usuarios", usuarioServicio.listarUsuarios());
        model.addAttribute("entrenadores", usuarioServicio.listarEntrenadoresActivos());
        model.addAttribute("usuario", usuario);
        return "gestion-usuarios";
    }

    @PostMapping("/crear")
    public String crearUsuario(
            @ModelAttribute Usuario usuario,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (usuarioServicio.existeEmail(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                return "redirect:/admin/usuarios";
            }

            usuarioServicio.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(
            @PathVariable Long id,
            @ModelAttribute Usuario usuario,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Usuario usuarioExistente = usuarioServicio.buscarPorId(id);
            if (usuarioExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/usuarios";
            }

            // Verificar email duplicado
            if (!usuarioExistente.getEmail().equals(usuario.getEmail())
                    && usuarioServicio.existeEmail(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está en uso");
                return "redirect:/admin/usuarios";
            }

            // Si no se proporciona password, mantener el existente
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            }

            usuario.setId(id);
            usuarioServicio.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            usuarioServicio.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}