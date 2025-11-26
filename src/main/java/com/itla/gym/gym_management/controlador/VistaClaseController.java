package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Clase;
import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.ClaseServicio;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/clases")
public class VistaClaseController {

    @Autowired
    private ClaseServicio claseServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String listarClases(Model model, HttpSession session) {
        // Verificar que sea admin
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.ADMIN) {
            return "redirect:/login";
        }

        List<Clase> todasClases = claseServicio.listarClases();
        long clasesActivas = claseServicio.contarClasesActivas();

        model.addAttribute("title", "Gestión de Clases");
        model.addAttribute("clases", todasClases);
        model.addAttribute("clasesActivas", clasesActivas);
        model.addAttribute("entrenadores", usuarioServicio.listarEntrenadoresActivos());
        model.addAttribute("usuario", usuario);
        return "gestion-clases";
    }

    @PostMapping("/crear")
    public String crearClase(
            @ModelAttribute Clase clase,
            RedirectAttributes redirectAttributes
    ) {
        try {
            claseServicio.guardarClase(clase);
            redirectAttributes.addFlashAttribute("success", "Clase creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }

    @PostMapping("/editar/{id}")
    public String editarClase(
            @PathVariable Long id,
            @ModelAttribute Clase clase,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Clase claseExistente = claseServicio.buscarPorId(id);
            if (claseExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Clase no encontrada");
                return "redirect:/admin/clases";
            }

            clase.setId(id);
            claseServicio.guardarClase(clase);
            redirectAttributes.addFlashAttribute("success", "Clase actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarClase(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            claseServicio.eliminarClase(id);
            redirectAttributes.addFlashAttribute("success", "Clase eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar clase: " + e.getMessage());
        }
        return "redirect:/admin/clases";
    }
}