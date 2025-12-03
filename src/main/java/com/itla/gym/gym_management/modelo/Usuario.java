package com.itla.gym.gym_management.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    private String password;

    private String telefono;

    @Column(name = "fecha_ingreso")
    private String fechaIngreso;

    private String objetivo;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private boolean activo = true;

    @Column(name = "id_entrenador")
    private Long idEntrenador;  // Para clientes: su entrenador asignado

    // Relación: Un cliente tiene un entrenador (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenador", insertable = false, updatable = false)
    private Usuario entrenador;

    public enum Rol {
        ADMIN,
        ENTRENADOR,
        CLIENTE
    }
}
