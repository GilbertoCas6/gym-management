package com.itla.gym.gym_management.repositorio;

import com.itla.gym.gym_management.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    List<Usuario> findByRol(Usuario.Rol rol);

    // Para encontrar todos los clientes de un entrenador específico
    List<Usuario> findByIdEntrenador(Long idEntrenador);

    // Para encontrar clientes activos
    List<Usuario> findByRolAndActivo(Usuario.Rol rol, boolean activo);
}