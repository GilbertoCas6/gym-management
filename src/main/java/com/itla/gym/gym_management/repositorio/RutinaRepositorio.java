package com.itla.gym.gym_management.repositorio;



import com.itla.gym.gym_management.modelo.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaRepositorio extends JpaRepository<Rutina, Long> {

    List<Rutina> findByActiva(Boolean activa);

    List<Rutina> findByIdCliente(Long idCliente);

    List<Rutina> findByIdEntrenador(Long idEntrenador);
}

