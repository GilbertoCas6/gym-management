package com.itla.gym.gym_management.repositorio;

import com.itla.gym.gym_management.modelo.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaseRepositorio extends JpaRepository<Clase, Long> {

    // Buscar clases por entrenador
    List<Clase> findByIdEntrenador(Long idEntrenador);

    // Buscar clases activas
    List<Clase> findByActiva(Boolean activa);

    // Buscar clases por día de la semana
    List<Clase> findByDiaSemana(String diaSemana);

    // Buscar clases activas de un entrenador específico
    List<Clase> findByIdEntrenadorAndActiva(Long idEntrenador, Boolean activa);
}