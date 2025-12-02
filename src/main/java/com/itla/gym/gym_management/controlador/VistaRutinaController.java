package com.itla.gym.gym_management.controlador;



import com.itla.gym.gym_management.modelo.Rutina;
import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.RutinaServicio;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/rutinas")
public class VistaRutinaController {

    @Autowired
    private RutinaServicio rutinaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String listarRutinas(Model model, HttpSession session) {
        // Solo admin
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.ADMIN) {
            return "redirect:/login";
        }

        List<Rutina> rutinas = rutinaServicio.listarRutinas();
        long rutinasActivas = rutinaServicio.contarRutinasActivas();

        model.addAttribute("title", "Gestión de Rutinas");
        model.addAttribute("rutinas", rutinas);
        model.addAttribute("rutinasActivas", rutinasActivas);
        model.addAttribute("clientes", usuarioServicio.listarClientesActivos());
        model.addAttribute("entrenadores", usuarioServicio.listarEntrenadoresActivos());
        model.addAttribute("usuario", usuario);

        // Para el formulario de creación
        model.addAttribute("nuevaRutina", new Rutina());

        return "gestion-rutinas";
    }

    @PostMapping("/crear")
    public String crearRutina(
            @ModelAttribute("nuevaRutina") Rutina rutina,
            RedirectAttributes redirectAttributes
    ) {
        try {
            rutinaServicio.guardarRutina(rutina);
            redirectAttributes.addFlashAttribute("success", "Rutina creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear rutina: " + e.getMessage());
        }
        return "redirect:/admin/rutinas";
    }

    @PostMapping("/editar/{id}")
    public String editarRutina(
            @PathVariable Long id,
            @ModelAttribute Rutina rutina,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Rutina existente = rutinaServicio.buscarPorId(id);
            if (existente == null) {
                redirectAttributes.addFlashAttribute("error", "Rutina no encontrada");
                return "redirect:/admin/rutinas";
            }

            rutina.setId(id);
            rutinaServicio.guardarRutina(rutina);
            redirectAttributes.addFlashAttribute("success", "Rutina actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar rutina: " + e.getMessage());
        }
        return "redirect:/admin/rutinas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarRutina(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            rutinaServicio.eliminarRutina(id);
            redirectAttributes.addFlashAttribute("success", "Rutina eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar rutina: " + e.getMessage());
        }
        return "redirect:/admin/rutinas";
    }

    @PostMapping("/toggle/{id}")
    public String cambiarEstado(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Rutina rutina = rutinaServicio.buscarPorId(id);
            if (rutina == null) {
                redirectAttributes.addFlashAttribute("error", "Rutina no encontrada");
                return "redirect:/admin/rutinas";
            }
            rutina.setActiva(!Boolean.TRUE.equals(rutina.getActiva()));
            rutinaServicio.guardarRutina(rutina);
            redirectAttributes.addFlashAttribute("success", "Estado de la rutina actualizado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/rutinas";
    }
}

