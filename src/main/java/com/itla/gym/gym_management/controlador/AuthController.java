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
public class AuthController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Mostrar página de login
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = usuarioServicio.autenticar(email, password);

        if (usuario != null && usuario.isActivo()) {
            // Guardar usuario en sesión
            session.setAttribute("usuario", usuario);
            session.setAttribute("rol", usuario.getRol());

            // Redirigir según rol
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas o usuario inactivo");
            return "redirect:/login";
        }
    }

    // Mostrar página de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String procesarRegistro(
            @ModelAttribute Usuario usuario,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Verificar si el email ya existe
            if (usuarioServicio.existeEmail(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                return "redirect:/registro";
            }

            // Guardar nuevo usuario
            usuarioServicio.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", "Cuenta creada exitosamente. Inicia sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la cuenta: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    // Dashboard principal
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        // Redirigir a dashboard específico según rol
        switch (usuario.getRol()) {
            case ADMIN:
                return "dashboard-admin";
            case ENTRENADOR:
                return "dashboard-entrenador";
            case SOCIO:
                return "dashboard-socio";
            default:
                return "redirect:/login";
        }
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}