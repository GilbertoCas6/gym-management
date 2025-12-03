package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.servicio.ClaseServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/cliente")
public class VistaClienteController {

    @Autowired
    private ClaseServicio claseServicio;   // Usa tu servicio de clases (cambia el nombre si es distinto)

    // ---------- MI RUTINA ----------
    @GetMapping("/rutina")
    public String verMiRutina(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        // DEMO
        model.addAttribute("nombreRutina", "Rutina Full Body – Nivel Intermedio");
        model.addAttribute("objetivoRutina",
                usuario.getObjetivo() != null ? usuario.getObjetivo() : "Mejora general");
        model.addAttribute("entrenadorNombre", "Gaudi Valera");
        model.addAttribute("semanasRutina", 12);

        return "cliente-rutina";
    }

    // ---------- INSCRIBIR CLASE ----------
    @GetMapping("/clases")
    public String verClasesCliente(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        // Usa el método que tengas para listar clases (si solo tienes listarClases(), úsalo ahí)
        model.addAttribute("clases", claseServicio.listarClasesActivas());

        return "cliente-clases";
    }

    @PostMapping("/clases/inscribir/{id}")
    public String inscribirseClase(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // DEMO: solo mensaje, no guarda en DB
        redirectAttributes.addFlashAttribute(
                "success",
                "Inscripción registrada a la clase #" + id + " (solo demostración visual)."
        );

        return "redirect:/cliente/clases";
    }

    // ---------- VER PROGRESO ----------
    @GetMapping("/progreso")
    public String verProgreso(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        LocalDate hoy = LocalDate.now();
        LocalDate inicio;

        // 1) Tomamos fechaIngreso si existe y es válida
        try {
            if (usuario.getFechaIngreso() != null && !usuario.getFechaIngreso().isEmpty()) {
                inicio = LocalDate.parse(usuario.getFechaIngreso());

                // Si la fecha de ingreso es en el futuro, la corregimos a hoy
                if (inicio.isAfter(hoy)) {
                    inicio = hoy;
                }
            } else {
                // Si no tiene fecha, usamos hoy (cliente nuevo)
                inicio = hoy;
            }
        } catch (DateTimeParseException e) {
            // Si la fecha está mal guardada, usamos hoy
            inicio = hoy;
        }

        long diasActivos = ChronoUnit.DAYS.between(inicio, hoy);
        if (diasActivos < 0) {
            diasActivos = 0; // seguridad extra
        }

        long semanasActivas = diasActivos / 7;

        String objetivo = usuario.getObjetivo() != null ? usuario.getObjetivo() : "Salud general";

        // DEMO: cambio estimado en LIBRAS según objetivo
        double cambioLibras = 0.0;
        if ("Pérdida de peso".equalsIgnoreCase(objetivo) || "Perdida de peso".equalsIgnoreCase(objetivo)) {
            cambioLibras = -0.5 * semanasActivas;   // -0.5 lb por semana
        } else if ("Ganancia muscular".equalsIgnoreCase(objetivo)) {
            cambioLibras = 0.25 * semanasActivas;    // +0.25 lb por semana
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("diasActivos", diasActivos);
        model.addAttribute("semanasActivas", semanasActivas);
        model.addAttribute("objetivo", objetivo);
        model.addAttribute("cambioLibras", cambioLibras);

        return "cliente-progreso";
    }

    // ---------- MIS PAGOS ----------
    @GetMapping("/pagos")
    public String verPagos(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        // Calculamos si es solo gym o gym + entrenador
        boolean tieneEntrenador = usuario.getIdEntrenador() != null;

        String incluye = tieneEntrenador ? "Gym + Entrenador" : "Solo Gym";
        int monto = tieneEntrenador ? 2500 : 1500;

        // Fecha de inscripción (si no tiene, usamos hoy como demo)
        String fechaInscripcionStr = (usuario.getFechaIngreso() != null && !usuario.getFechaIngreso().isEmpty())
                ? usuario.getFechaIngreso()
                : LocalDate.now().toString();

        LocalDate fechaInscripcion = LocalDate.parse(fechaInscripcionStr);
        LocalDate proximoPago = fechaInscripcion.plusMonths(1);

        // Datos para la tarjeta de resumen
        model.addAttribute("incluye", incluye);
        model.addAttribute("monto", monto);
        model.addAttribute("fechaInscripcion", fechaInscripcionStr);
        model.addAttribute("estadoPago", "Pagado");
        model.addAttribute("proximoPago", proximoPago.toString());

        // Historial de pagos: SOLO el pago realizado (sin pendiente)
        java.util.List<java.util.Map<String, Object>> historial = new java.util.ArrayList<>();

        java.util.Map<String, Object> pagoRealizado = new java.util.HashMap<>();
        pagoRealizado.put("fecha", fechaInscripcionStr);
        pagoRealizado.put("monto", monto);
        pagoRealizado.put("estado", "Pagado");

        historial.add(pagoRealizado);

        model.addAttribute("historialPagos", historial);

        return "cliente-pagos";
    }

}
