package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.sindico.SindicoDTO;
import com.example.inovaTest.models.SindicoModel;
import java.util.UUID;

public class SindicoMapper {
    public static SindicoDTO toDTO(SindicoModel model) {
        if (model == null) return null;
        SindicoDTO dto = new SindicoDTO();
        dto.setId(model.getId());
        dto.setCondominioId(model.getCondominio() != null ? model.getCondominio().getId() : null);
        dto.setUsuarioId(model.getUsuario() != null ? model.getUsuario().getId() : null);
        dto.setNome(model.getNome());
        dto.setCpf(model.getCpf());
        dto.setTelefone(model.getTelefone());
        dto.setEmail(model.getEmail());
        dto.setAtivo(model.getAtivo());
        return dto;
    }

    public static SindicoModel toModel(SindicoDTO dto) {
        if (dto == null) return null;
        SindicoModel model = new SindicoModel();
        model.setId(dto.getId());
        // condominio e usuario devem ser setados no service/controller
        model.setNome(dto.getNome());
        model.setCpf(dto.getCpf());
        model.setTelefone(dto.getTelefone());
        model.setEmail(dto.getEmail());
        model.setAtivo(dto.getAtivo());
        return model;
    }
}
