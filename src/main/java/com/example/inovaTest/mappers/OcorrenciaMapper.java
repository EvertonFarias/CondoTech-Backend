package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.ocorrencia.OcorrenciaDTO;
import com.example.inovaTest.models.OcorrenciaModel;

public class OcorrenciaMapper {
    public static OcorrenciaDTO toDTO(OcorrenciaModel model) {
        if (model == null) return null;
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(model.getId());
        dto.setMoradorId(model.getMorador() != null ? model.getMorador().getId() : null);
        dto.setMoradorNome(model.getMorador() != null ? model.getMorador().getNome() : null);
        dto.setTipoOcorrencia(model.getTipoOcorrencia());
        dto.setTitulo(model.getTitulo());
        dto.setDescricao(model.getDescricao());
        dto.setStatusOcorrencia(model.getStatusOcorrencia());
        dto.setRespostaSindico(model.getRespostaSindico());
        dto.setImageUrl(model.getImageUrl());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setDataResolucao(model.getDataResolucao());
        dto.setUpdatedAt(model.getUpdatedAt());
        return dto;
    }

    public static OcorrenciaModel toModel(OcorrenciaDTO dto) {
        if (dto == null) return null;
        OcorrenciaModel model = new OcorrenciaModel();
        model.setId(dto.getId());
        model.setTipoOcorrencia(dto.getTipoOcorrencia());
        model.setTitulo(dto.getTitulo());
        model.setDescricao(dto.getDescricao());
        model.setStatusOcorrencia(dto.getStatusOcorrencia());
        model.setRespostaSindico(dto.getRespostaSindico());
        model.setImageUrl(dto.getImageUrl());
        model.setDataResolucao(dto.getDataResolucao());
        return model;
    }
}