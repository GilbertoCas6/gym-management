package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.servicio.SocioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/socios")
public class VistaSocioController {

    @Autowired
    private SocioServicio socioServicio;

    @GetMapping
    public String listarSocios(Model model) {
        model.addAttribute("title", "Gestión de Socios");
        model.addAttribute("socios", socioServicio.listarSocios());
        return "socios"; // archivo socios.html en templates
    }
}
