package com.itla.gym.gym_management.repositorio;

import com.itla.gym.gym_management.modelo.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepositorio extends JpaRepository<Socio, Long> {
}
