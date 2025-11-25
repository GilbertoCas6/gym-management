package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

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

    @GetMapping("/dashboard/admin")
    public String dashboardAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Usuario.Rol.ADMIN) {
            return "redirect:/login";
        }

        // Estadísticas para el dashboard
        List<Usuario> todosUsuarios = usuarioServicio.listarUsuarios();
        List<Usuario> clientes = usuarioServicio.listarPorRol(Usuario.Rol.CLIENTE);
        List<Usuario> entrenadores = usuarioServicio.listarPorRol(Usuario.Rol.ENTRENADOR);
        List<Usuario> clientesActivos = usuarioServicio.listarClientesActivos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("totalUsuarios", todosUsuarios.size());
        model.addAttribute("totalClientes", clientes.size());
        model.addAttribute("totalEntrenadores", entrenadores.size());
        model.addAttribute("clientesActivos", clientesActivos.size());

        return "dashboard-admin";
    }

    @GetMapping("/dashboard/entrenador")
    public String dashboardEntrenador(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        // Obtener clientes asignados a este entrenador
        List<Usuario> misClientes = usuarioServicio.listarClientesDeEntrenador(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("misClientes", misClientes);
        model.addAttribute("totalClientes", misClientes.size());

        return "dashboard-entrenador";
    }

    @GetMapping("/dashboard/cliente")
    public String dashboardCliente(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getRol() != Usuario.Rol.CLIENTE) {
            return "redirect:/login";
        }

        // Obtener el entrenador asignado (si tiene)
        Usuario entrenador = null;
        if (usuario.getIdEntrenador() != null) {
            entrenador = usuarioServicio.buscarPorId(usuario.getIdEntrenador());
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("entrenador", entrenador);

        return "dashboard-cliente";
    }
}