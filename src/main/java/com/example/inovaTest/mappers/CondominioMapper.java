package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.condominio.CondominioDTO;
import com.example.inovaTest.models.CondominioModel;

public class CondominioMapper {
    public static CondominioDTO toDTO(CondominioModel model) {
        if (model == null) return null;
        CondominioDTO dto = new CondominioDTO();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setEndereco(model.getEndereco());
        dto.setTelefone(model.getTelefone());
        dto.setEmail(model.getEmail());
        dto.setCnpj(model.getCnpj());
        return dto;
    }

    public static CondominioModel toModel(CondominioDTO dto) {
        if (dto == null) return null;
        CondominioModel model = new CondominioModel();
        model.setId(dto.getId());
        model.setNome(dto.getNome());
        model.setEndereco(dto.getEndereco());
        model.setTelefone(dto.getTelefone());
        model.setEmail(dto.getEmail());
        model.setCnpj(dto.getCnpj());
        return model;
    }
}
