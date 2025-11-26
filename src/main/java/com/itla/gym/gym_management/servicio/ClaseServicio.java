package com.itla.gym.gym_management.servicio;

import com.itla.gym.gym_management.modelo.Clase;
import com.itla.gym.gym_management.repositorio.ClaseRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseServicio {

    @Autowired
    private ClaseRepositorio claseRepositorio;

    public List<Clase> listarClases() {
        return claseRepositorio.findAll();
    }

    public List<Clase> listarClasesActivas() {
        return claseRepositorio.findByActiva(true);
    }

    public List<Clase> listarClasesPorEntrenador(Long idEntrenador) {
        return claseRepositorio.findByIdEntrenador(idEntrenador);
    }

    public List<Clase> listarClasesPorDia(String diaSemana) {
        return claseRepositorio.findByDiaSemana(diaSemana);
    }

    public List<Clase> listarClasesActivasPorEntrenador(Long idEntrenador) {
        return claseRepositorio.findByIdEntrenadorAndActiva(idEntrenador, true);
    }

    public Clase guardarClase(Clase clase) {
        return claseRepositorio.save(clase);
    }

    public Clase buscarPorId(Long id) {
        return claseRepositorio.findById(id).orElse(null);
    }

    public void eliminarClase(Long id) {
        claseRepositorio.deleteById(id);
    }

    public long contarClasesActivas() {
        return claseRepositorio.findByActiva(true).size();
    }
}