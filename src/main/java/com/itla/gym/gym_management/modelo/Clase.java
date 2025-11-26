package com.itla.gym.gym_management.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clases")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @Column(name = "id_entrenador")
    private Long idEntrenador;

    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "hora_inicio")
    private String horaInicio;

    private Integer duracion; // duración en minutos

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    private Boolean activa = true;

    // Relación: Una clase tiene un entrenador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenador", insertable = false, updatable = false)
    private Usuario entrenador;
}