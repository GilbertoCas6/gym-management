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
public class VistaPagoController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/admin/pagos")
    public String mostrarPagos(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        // Clientes activos
        List<Usuario> clientesActivos = usuarioServicio.listarClientesActivos();
        model.addAttribute("clientesActivos", clientesActivos);

        int montoSoloGym = 1500;
        int montoGymEntrenador = 2500;

        int totalMembresiasActivas = clientesActivos.size();
        int ingresosMensualesEstimados = 0;

        for (Usuario c : clientesActivos) {
            if (c.getIdEntrenador() == null) {
                ingresosMensualesEstimados += montoSoloGym;
            } else {
                ingresosMensualesEstimados += montoGymEntrenador;
            }
        }

        model.addAttribute("totalMembresiasActivas", totalMembresiasActivas);
        model.addAttribute("ingresosMensualesEstimados", ingresosMensualesEstimados);
        model.addAttribute("pagosPendientes", 0);

        model.addAttribute("montoSoloGym", montoSoloGym);
        model.addAttribute("montoGymEntrenador", montoGymEntrenador);

        return "gestion-pagos";
    }
}
