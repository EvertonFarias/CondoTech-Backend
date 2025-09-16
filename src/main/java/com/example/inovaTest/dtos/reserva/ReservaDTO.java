package com.example.inovaTest.dtos.reserva;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaDTO {
    private Long id;
    private Long moradorId;
    private Long areaComumId;
    private LocalDate dataReserva;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String statusReserva;
    private String observacoes;
}
