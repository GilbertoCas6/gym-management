package com.itla.gym.gym_management.controlador;

import com.itla.gym.gym_management.modelo.Socio;
import com.itla.gym.gym_management.servicio.SocioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioControlador {

    @Autowired
    private SocioServicio socioServicio;

    @GetMapping
    public List<Socio> listar() {
        return socioServicio.listarSocios();
    }

    @PostMapping
    public Socio guardar(@RequestBody Socio socio) {
        return socioServicio.guardarSocio(socio);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        socioServicio.eliminarSocio(id);
    }

    @GetMapping("/ping")
    public String ping() {
        return "Controlador de socios activo ✅";
    }

}
