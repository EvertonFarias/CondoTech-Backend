package com.example.inovaTest.models;

import com.example.inovaTest.enums.StatusReserva; // 1. Importe o novo Enum
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReservaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_morador", nullable = false)
    private MoradorModel morador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_comum", nullable = false)
    private AreaComumModel areaComum;

    @Column(nullable = false)
    @NotNull(message = "Data da reserva é obrigatória")
    private LocalDate dataReserva;

    @Column(nullable = false)
    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @Column(nullable = false)
    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status;
}