package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // ---------- LOGIN ----------
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Usuario usuario = usuarioServicio.buscarPorEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {

            if (!usuario.isActivo()) {
                model.addAttribute("error", "Tu cuenta está inactiva. Contacta al administrador.");
                return "login";
            }

            // Guardar en sesión
            session.setAttribute("usuario", usuario);

            // Redirigir según el rol
            switch (usuario.getRol()) {
                case ADMIN:
                    return "redirect:/dashboard/admin";
                case ENTRENADOR:
                    return "redirect:/dashboard/entrenador";
                case CLIENTE:
                    return "redirect:/dashboard/cliente";
                default:
                    return "redirect:/login";
            }
        }

        model.addAttribute("error", "Email o contraseña incorrectos");
        return "login";
    }

    // ---------- LOGOUT ----------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ---------- REGISTRO ----------
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String telefono,
            Model model
    ) {
        if (usuarioServicio.existeEmail(email)) {
            model.addAttribute("error", "El email ya está registrado");
            return "registro";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setRol(Usuario.Rol.CLIENTE); // Registros públicos = CLIENTES
        nuevoUsuario.setActivo(true);

        usuarioServicio.guardarUsuario(nuevoUsuario);

        model.addAttribute("success", "Registro exitoso. Ya puedes iniciar sesión.");
        return "login";
    }
}
