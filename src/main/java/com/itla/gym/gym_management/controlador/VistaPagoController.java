package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VistaPagoController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/admin/pagos")
    public String mostrarPagos(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);

        List<Usuario> clientes = usuarioServicio.obtenerClientes();
        model.addAttribute("clientes", clientes);

// total de membresías activas = cantidad de clientes
        int totalMembresiasActivas = clientes.size();
        model.addAttribute("totalMembresiasActivas", totalMembresiasActivas);

// ingreso mensual estimado: asumimos $1500 por cliente
        int ingresosMensualesEstimados = totalMembresiasActivas * 1500;
        model.addAttribute("ingresosMensualesEstimados", ingresosMensualesEstimados);

// pagos pendientes: por ahora 0 (demo)
        int pagosPendientes = 0;
        model.addAttribute("pagosPendientes", pagosPendientes);


        return "gestion-pagos";
    }

    @PostMapping("/admin/pagos/simular")
    public String simularPago(RedirectAttributes redirectAttributes) {

        // Solo demo
        redirectAttributes.addFlashAttribute(
                "success",
                "Pago simulado correctamente (solo demostración)."
        );

        return "redirect:/admin/pagos";
    }
}
