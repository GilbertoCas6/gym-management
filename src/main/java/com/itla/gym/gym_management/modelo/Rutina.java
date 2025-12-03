package com.itla.gym.gym_management.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "id_entrenador")
    private Long idEntrenador;

    private String objetivo;

    @Column(name = "dias_semana")
    private String diasSemana;   // Ej: "Lunes,Miércoles,Viernes"

    @Column(name = "duracion_semanas")
    private Integer duracionSemanas;

    private Boolean activa = true;

    // Relaciones para poder mostrar nombres en las vistas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", insertable = false, updatable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenador", insertable = false, updatable = false)
    private Usuario entrenador;
}

