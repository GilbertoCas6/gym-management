package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Usuario;
import com.itla.gym.gym_management.modelo.Clase;
import com.itla.gym.gym_management.servicio.UsuarioServicio;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class VistaEntrenadorController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClaseServicio claseServicio;

    // ------- DTOs para las vistas --------
    public static class ProgresoClienteDTO {
        private final Usuario cliente;
        private final long semanasActivas;
        private final double cambioLibras;

        public ProgresoClienteDTO(Usuario cliente, long semanasActivas, double cambioLibras) {
            this.cliente = cliente;
            this.semanasActivas = semanasActivas;
            this.cambioLibras = cambioLibras;
        }

        public Usuario getCliente() { return cliente; }
        public long getSemanasActivas() { return semanasActivas; }
        public double getCambioLibras() { return cambioLibras; }
    }

    public static class AsistenciaClienteDTO {
        private final Usuario cliente;
        private final long semanasActivas;
        private final int sesionesPlanificadas;
        private final int sesionesAsistidasEstimadas;

        public AsistenciaClienteDTO(Usuario cliente,
                                    long semanasActivas,
                                    int sesionesPlanificadas,
                                    int sesionesAsistidasEstimadas) {
            this.cliente = cliente;
            this.semanasActivas = semanasActivas;
            this.sesionesPlanificadas = sesionesPlanificadas;
            this.sesionesAsistidasEstimadas = sesionesAsistidasEstimadas;
        }

        public Usuario getCliente() { return cliente; }
        public long getSemanasActivas() { return semanasActivas; }
        public int getSesionesPlanificadas() { return sesionesPlanificadas; }
        public int getSesionesAsistidasEstimadas() { return sesionesAsistidasEstimadas; }
    }

    // ===================== PANEL PRINCIPAL =====================
    @GetMapping("/dashboard/entrenador")
    public String verPanelEntrenador(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        List<Usuario> misClientes = usuarioServicio.listarClientesDeEntrenador(usuario.getId());
        model.addAttribute("misClientes", misClientes);

        model.addAttribute("totalClientes", misClientes.size());
        model.addAttribute("clasesHoy", 0);      // demo
        model.addAttribute("rutinasActivas", 0); // demo

        return "dashboard-entrenador";
    }

    // ===================== VER DETALLE CLIENTE =====================
    @GetMapping("/entrenador/clientes/{id}")
    public String verDetalleCliente(@PathVariable Long id,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        Usuario cliente = usuarioServicio.buscarPorId(id);

        // Validar que exista y esté asignado a este entrenador
        if (cliente == null ||
                cliente.getIdEntrenador() == null ||
                !cliente.getIdEntrenador().equals(entrenador.getId())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Cliente no encontrado o no está asignado a ti."
            );
            return "redirect:/dashboard/entrenador";
        }

        model.addAttribute("usuario", entrenador);
        model.addAttribute("cliente", cliente);

        return "entrenador-cliente-detalle";
    }

    // ===================== DESASIGNAR CLIENTE =====================
    @PostMapping("/entrenador/clientes/{id}/desasignar")
    public String desasignarCliente(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        Usuario cliente = usuarioServicio.buscarPorId(id);
        if (cliente == null ||
                cliente.getIdEntrenador() == null ||
                !cliente.getIdEntrenador().equals(entrenador.getId())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Cliente no encontrado o no está asignado a ti."
            );
            return "redirect:/dashboard/entrenador";
        }

        // Quitar relación con el entrenador
        cliente.setIdEntrenador(null);
        usuarioServicio.guardarUsuario(cliente);

        redirectAttributes.addFlashAttribute(
                "success",
                "Cliente desasignado correctamente."
        );

        return "redirect:/dashboard/entrenador";
    }

    // ===================== ACCESO: NUEVA RUTINA =====================
    @GetMapping("/entrenador/rutinas/nueva")
    public String nuevaRutinaEntrenador(HttpSession session, Model model) {
        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", entrenador);
        model.addAttribute("misClientes",
                usuarioServicio.listarClientesDeEntrenador(entrenador.getId()));

        return "entrenador-rutina-nueva";
    }

    @PostMapping("/entrenador/rutinas/crear")
    public String crearRutinaEntrenador(@RequestParam Long idCliente,
                                        @RequestParam String nombreRutina,
                                        @RequestParam(required = false) Integer semanas,
                                        @RequestParam(required = false) String objetivo,
                                        @RequestParam(required = false) String notas,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {

        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        Usuario cliente = usuarioServicio.buscarPorId(idCliente);
        if (cliente == null ||
                cliente.getIdEntrenador() == null ||
                !cliente.getIdEntrenador().equals(entrenador.getId())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Cliente no encontrado o no está asignado a ti."
            );
            return "redirect:/dashboard/entrenador";
        }

        if (semanas == null || semanas <= 0) {
            semanas = 8;
        }
        if (objetivo == null || objetivo.isBlank()) {
            objetivo = cliente.getObjetivo() != null ? cliente.getObjetivo() : "Mejora general";
        }

        String msg = "Rutina \"" + nombreRutina + "\" creada para el cliente "
                + cliente.getNombre() + " (" + semanas + " semanas, objetivo: " + objetivo + ").";
        redirectAttributes.addFlashAttribute("success", msg);

        return "redirect:/entrenador/rutinas/nueva";
    }

    // ===================== ACCESO: CREAR CLASE =====================
    @GetMapping("/entrenador/clases/nueva")
    public String nuevaClaseEntrenador(HttpSession session, Model model) {
        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", entrenador);
        return "entrenador-clase-nueva";
    }

    @PostMapping("/entrenador/clases/crear")
    public String crearClaseEntrenador(@RequestParam("nombreClase") String nombreClase,
                                       @RequestParam("fecha") String fechaStr,
                                       @RequestParam(value = "descripcion", required = false) String descripcion,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {

        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        // Convertimos la fecha recibida
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (Exception e) {
            fecha = LocalDate.now();
        }

        if (descripcion == null) {
            descripcion = "";
        }

        // CREACIÓN REAL DE LA CLASE
        Clase clase = new Clase();
        clase.setNombre(nombreClase);
        clase.setDescripcion("Fecha: " + fecha + " - " + descripcion);
        clase.setIdEntrenador(entrenador.getId());
        clase.setActiva(true);

        // Campos obligatorios no nulos
        clase.setCapacidadMaxima(20);                       // por defecto
        clase.setDiaSemana(fecha.getDayOfWeek().toString()); // LUNES, MARTES, etc.
        clase.setDuracion(60);                              // 60 minutos
        clase.setHoraInicio("18:00");                       // como String en tu entidad

        claseServicio.guardarClase(clase);

        redirectAttributes.addFlashAttribute(
                "success",
                "Clase \"" + nombreClase + "\" creada correctamente para el día " + fecha + "."
        );

        return "redirect:/dashboard/entrenador";
    }

    // ===================== ACCESO: PROGRESO CLIENTES =====================
    @GetMapping("/entrenador/progreso")
    public String verProgresoClientes(HttpSession session, Model model) {
        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        List<Usuario> misClientes = usuarioServicio.listarClientesDeEntrenador(entrenador.getId());
        List<ProgresoClienteDTO> progresos = new ArrayList<>();

        LocalDate hoy = LocalDate.now();

        for (Usuario c : misClientes) {
            LocalDate inicio = obtenerFechaInicio(c, hoy);
            long dias = ChronoUnit.DAYS.between(inicio, hoy);
            if (dias < 0) dias = 0;
            long semanas = Math.max(1, dias / 7);

            String objetivo = c.getObjetivo() != null ? c.getObjetivo() : "Salud general";

            double cambioLibras = 0.0;
            if ("Pérdida de peso".equalsIgnoreCase(objetivo)) {
                cambioLibras = -1.0 * semanas;
            } else if ("Ganancia muscular".equalsIgnoreCase(objetivo)) {
                cambioLibras = 0.5 * semanas;
            }

            progresos.add(new ProgresoClienteDTO(c, semanas, cambioLibras));
        }

        model.addAttribute("usuario", entrenador);
        model.addAttribute("progresos", progresos);

        return "entrenador-progreso";
    }

    // ===================== ASISTENCIAS =====================
    // ===================== ASISTENCIAS =====================
    @GetMapping("/entrenador/asistencias")
    public String verAsistencias(HttpSession session, Model model) {

        Usuario entrenador = (Usuario) session.getAttribute("usuario");
        if (entrenador == null || entrenador.getRol() != Usuario.Rol.ENTRENADOR) {
            return "redirect:/login";
        }

        List<Usuario> misClientes = usuarioServicio.listarClientesDeEntrenador(entrenador.getId());
        List<AsistenciaClienteDTO> asistencias = new ArrayList<>();

        LocalDate hoy = LocalDate.now();

        for (Usuario c : misClientes) {

            LocalDate inicio = obtenerFechaInicio(c, hoy);

            long dias = ChronoUnit.DAYS.between(inicio, hoy);
            if (dias < 0) dias = 0;
            long semanas = Math.max(1, dias / 7);

            // Plan fijo: 3 sesiones por semana para todos
            int sesionesPlanificadas = (int) (semanas * 3);

            // Para este proyecto, asumimos que asistió a todas las planificadas
            int sesionesAsistidas = 0;


            asistencias.add(new AsistenciaClienteDTO(
                    c,
                    semanas,
                    sesionesPlanificadas,
                    sesionesAsistidas
            ));
        }

        model.addAttribute("usuario", entrenador);
        model.addAttribute("asistencias", asistencias);

        return "entrenador-asistencias";
    }


    // ===================== HELPER =====================
    private LocalDate obtenerFechaInicio(Usuario usuario, LocalDate fallback) {
        try {
            if (usuario.getFechaIngreso() != null && !usuario.getFechaIngreso().isEmpty()) {
                LocalDate f = LocalDate.parse(usuario.getFechaIngreso());
                if (!f.isAfter(LocalDate.now())) {
                    return f;
                }
            }
        } catch (DateTimeParseException ignored) {
        }
        return fallback.minusWeeks(4);
    }

}
