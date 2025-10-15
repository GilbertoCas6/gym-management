package com.itla.gym.gym_management.servicio;

import com.itla.gym.gym_management.modelo.Socio;
import com.itla.gym.gym_management.repositorio.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioServicio {

    @Autowired
    private SocioRepositorio socioRepositorio;

    public List<Socio> listarSocios() {
        return socioRepositorio.findAll();
    }

    public Socio guardarSocio(Socio socio) {
        return socioRepositorio.save(socio);
    }

    public void eliminarSocio(Long id) {
        socioRepositorio.deleteById(id);
    }
}
