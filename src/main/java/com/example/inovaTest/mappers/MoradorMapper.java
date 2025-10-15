package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.morador.MoradorDTO;
import com.example.inovaTest.models.MoradorModel;

public class MoradorMapper {
    public static MoradorDTO toDTO(MoradorModel model) {
        if (model == null) return null;
        MoradorDTO dto = new MoradorDTO();
        dto.setId(model.getId());
        dto.setUnidadeId(model.getUnidade() != null ? model.getUnidade().getId() : null);
        dto.setUsuarioId(model.getUsuario() != null ? model.getUsuario().getId() : null);
        dto.setNome(model.getNome());
        dto.setCpf(model.getCpf());
        dto.setTelefone(model.getTelefone());
        dto.setEmail(model.getEmail());
        dto.setTipoMorador(model.getTipo());
        dto.setAtivo(model.getAtivo());
        return dto;
    }

    public static MoradorModel toModel(MoradorDTO dto) {
        if (dto == null) return null;
        MoradorModel model = new MoradorModel();
        model.setId(dto.getId());
        // unidade e usuario devem ser setados no service/controller
        model.setNome(dto.getNome());
        model.setCpf(dto.getCpf());
        model.setTelefone(dto.getTelefone());
        model.setEmail(dto.getEmail());
        model.setTipo(dto.getTipoMorador());
        model.setAtivo(dto.getAtivo());
        return model;
    }
}
