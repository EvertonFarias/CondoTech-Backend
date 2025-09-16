package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.reserva.ReservaDTO;
import com.example.inovaTest.models.ReservaModel;

public class ReservaMapper {
    public static ReservaDTO toDTO(ReservaModel model) {
        if (model == null) return null;
        ReservaDTO dto = new ReservaDTO();
        dto.setId(model.getId());
        dto.setMoradorId(model.getMorador() != null ? model.getMorador().getId() : null);
        dto.setAreaComumId(model.getAreaComum() != null ? model.getAreaComum().getId() : null);
        dto.setDataReserva(model.getDataReserva());
        dto.setHoraInicio(model.getHoraInicio());
        dto.setHoraFim(model.getHoraFim());
        dto.setStatusReserva(model.getStatusReserva());
        dto.setObservacoes(model.getObservacoes());
        return dto;
    }

    public static ReservaModel toModel(ReservaDTO dto) {
        if (dto == null) return null;
        ReservaModel model = new ReservaModel();
        model.setId(dto.getId());
        // morador e areaComum devem ser setados no service/controller
        model.setDataReserva(dto.getDataReserva());
        model.setHoraInicio(dto.getHoraInicio());
        model.setHoraFim(dto.getHoraFim());
        model.setStatusReserva(dto.getStatusReserva());
        model.setObservacoes(dto.getObservacoes());
        return model;
    }
}
