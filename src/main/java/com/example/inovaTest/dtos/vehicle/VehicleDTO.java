package com.example.inovaTest.dtos.vehicle;

import com.example.inovaTest.models.VehicleModel;

public record VehicleDTO(
    Long id,
    String placa,
    String marca,
    String modelo,
    String cor
) {
    public VehicleDTO(VehicleModel vehicle) {
        this(
            vehicle.getId(),
            vehicle.getPlaca(),
            vehicle.getMarca(),
            vehicle.getModelo(),
            vehicle.getCor()
        );
    }
}