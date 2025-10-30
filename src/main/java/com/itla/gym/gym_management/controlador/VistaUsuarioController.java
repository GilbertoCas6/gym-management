package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/usuarios")
public class VistaUsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("title", "Gestión de Usuarios");
        model.addAttribute("usuarios", usuarioServicio.listarUsuarios());
        return "usuarios"; // archivo usuarios.html en templates
    }
}
