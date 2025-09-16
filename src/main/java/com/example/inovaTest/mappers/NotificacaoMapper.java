package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.notificacao.NotificacaoDTO;
import com.example.inovaTest.models.NotificacaoModel;

public class NotificacaoMapper {
    public static NotificacaoDTO toDTO(NotificacaoModel model) {
        if (model == null) return null;
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setId(model.getId());
        dto.setMoradorId(model.getMorador() != null ? model.getMorador().getId() : null);
        dto.setTitulo(model.getTitulo());
        dto.setMensagem(model.getMensagem());
        dto.setLida(model.getLida());
        return dto;
    }

    public static NotificacaoModel toModel(NotificacaoDTO dto) {
        if (dto == null) return null;
        NotificacaoModel model = new NotificacaoModel();
        model.setId(dto.getId());
        // morador deve ser setado no service/controller
        model.setTitulo(dto.getTitulo());
        model.setMensagem(dto.getMensagem());
        model.setLida(dto.getLida());
        return model;
    }
}
