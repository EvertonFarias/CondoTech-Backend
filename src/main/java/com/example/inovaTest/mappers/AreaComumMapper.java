package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.areaComum.AreaComumDTO;
import com.example.inovaTest.models.AreaComumModel;

public class AreaComumMapper {
    public static AreaComumDTO toDTO(AreaComumModel model) {
        if (model == null) return null;
        AreaComumDTO dto = new AreaComumDTO();
        dto.setId(model.getId());
        dto.setCondominioId(model.getCondominio() != null ? model.getCondominio().getId() : null);
        dto.setNome(model.getNome());
        dto.setDescricao(model.getDescricao());
        dto.setCapacidadeMaxima(model.getCapacidadeMaxima());
        dto.setValorTaxa(model.getValorTaxa());
        dto.setAtiva(model.getAtiva());
        return dto;
    }

    public static AreaComumModel toModel(AreaComumDTO dto) {
        if (dto == null) return null;
        AreaComumModel model = new AreaComumModel();
        model.setId(dto.getId());
        // O campo condominio deve ser setado no service/controller
        model.setNome(dto.getNome());
        model.setDescricao(dto.getDescricao());
        model.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        model.setValorTaxa(dto.getValorTaxa());
        model.setAtiva(dto.getAtiva());
        return model;
    }
}
