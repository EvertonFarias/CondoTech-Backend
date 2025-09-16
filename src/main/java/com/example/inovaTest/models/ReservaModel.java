package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "id_area", nullable = false)
    private AreaComumModel areaComum;

    @Column(name = "data_reserva", nullable = false)
    @NotNull(message = "Data da reserva é obrigatória")
    private LocalDate dataReserva;

    @Column(name = "hora_inicio", nullable = false)
    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    @Column(name = "status_reserva", length = 20, nullable = false)
    @Size(max = 20, message = "Status da reserva deve ter no máximo 20 caracteres")
    private String statusReserva = "PENDENTE"; // PENDENTE, CONFIRMADA, CANCELADA, FINALIZADA

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}