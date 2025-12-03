package com.itla.gym.gym_management.controlador;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

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
    public String crearUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {

        try {
            // Si viene vacío desde el form, MySQL no acepta ""
            if (usuario.getFechaIngreso() != null && usuario.getFechaIngreso().trim().isEmpty()) {
                usuario.setFechaIngreso(null);
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
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String telefono,
            @RequestParam("rol") Usuario.Rol rol,
            @RequestParam(value = "fechaIngreso", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaIngreso,
            @RequestParam(required = false) String objetivo,
            @RequestParam(value = "idEntrenador", required = false) Long idEntrenador,
            @RequestParam(value = "activo", defaultValue = "false") boolean activo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Buscar el usuario existente
            Usuario usuario = usuarioServicio.buscarPorId(id);
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/admin/usuarios";
            }

            // Campos básicos
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setRol(rol);

            // Solo cambia la contraseña si el campo no viene vacío
            if (password != null && !password.isBlank()) {
                usuario.setPassword(password);
            }

            // Campos específicos de clientes
            usuario.setFechaIngreso(fechaIngreso != null ? fechaIngreso.toString() : null);

            usuario.setObjetivo(objetivo);
            usuario.setIdEntrenador(idEntrenador);

            // 🔴 IMPORTANTE: actualizar el estado activo
            usuario.setActivo(activo);

            // Guardar cambios
            usuarioServicio.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar usuario: " + e.getMessage());
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