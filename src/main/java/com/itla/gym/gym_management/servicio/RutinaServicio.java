package com.itla.gym.gym_management.servicio;


import com.itla.gym.gym_management.modelo.Rutina;
import com.itla.gym.gym_management.repositorio.RutinaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutinaServicio {

    @Autowired
    private RutinaRepositorio rutinaRepositorio;

    public List<Rutina> listarRutinas() {
        return rutinaRepositorio.findAll();
    }

    public List<Rutina> listarRutinasActivas() {
        return rutinaRepositorio.findByActiva(true);
    }

    public List<Rutina> listarPorCliente(Long idCliente) {
        return rutinaRepositorio.findByIdCliente(idCliente);
    }

    public List<Rutina> listarPorEntrenador(Long idEntrenador) {
        return rutinaRepositorio.findByIdEntrenador(idEntrenador);
    }

    public Rutina guardarRutina(Rutina rutina) {
        if (rutina.getActiva() == null) {
            rutina.setActiva(true);
        }
        return rutinaRepositorio.save(rutina);
    }

    public Rutina buscarPorId(Long id) {
        return rutinaRepositorio.findById(id).orElse(null);
    }

    public void eliminarRutina(Long id) {
        rutinaRepositorio.deleteById(id);
    }

    public long contarRutinasActivas() {
        return rutinaRepositorio.findByActiva(true).size();
    }
}
