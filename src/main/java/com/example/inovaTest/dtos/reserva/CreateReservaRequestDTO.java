package com.example.inovaTest.dtos.reserva;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


public record CreateReservaRequestDTO(
    @NotNull Long areaComumId,
    @NotNull LocalDate dataReserva,
    @NotNull LocalTime horaInicio,
    @NotNull LocalTime horaFim
) {}