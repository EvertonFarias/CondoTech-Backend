package com.example.inovaTest.dtos.reserva;

import com.example.inovaTest.enums.StatusReserva;
import com.example.inovaTest.models.ReservaModel;
import java.time.LocalDate;
import java.time.LocalTime;

// Este DTO representa uma reserva já existente, para ser exibida no front-end
public record ReservaDTO(
    Long id,
    Long areaComumId,
    String areaComumNome,
    Long moradorId,
    String moradorNome,
    LocalDate dataReserva,
    LocalTime horaInicio,
    LocalTime horaFim,
    StatusReserva status
) {
    public ReservaDTO(ReservaModel reserva) {
        this(
            reserva.getId(),
            reserva.getAreaComum().getId(),
            reserva.getAreaComum().getNome(),
            reserva.getMorador().getId(),
            reserva.getMorador().getNome(),
            reserva.getDataReserva(),
            reserva.getHoraInicio(),
            reserva.getHoraFim(),
            reserva.getStatus()
        );
    }
}