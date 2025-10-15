package com.itla.gym.gym_management.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "socio")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombre;
    private String email;

    @Column(name = "fecha_ingreso")
    private String fechaIngreso;

    private String estado;
    private String objetivo;
    private String telefono;
}
