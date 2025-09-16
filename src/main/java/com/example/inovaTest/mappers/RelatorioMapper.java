package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.relatorio.RelatorioDTO;
import com.example.inovaTest.models.RelatorioModel;

public class RelatorioMapper {
    public static RelatorioDTO toDTO(RelatorioModel model) {
        if (model == null) return null;
        RelatorioDTO dto = new RelatorioDTO();
        dto.setId(model.getId());
        dto.setCondominioId(model.getCondominio() != null ? model.getCondominio().getId() : null);
        dto.setTipoRelatorio(model.getTipoRelatorio());
        dto.setPeriodoInicio(model.getPeriodoInicio());
        dto.setPeriodoFim(model.getPeriodoFim());
        dto.setDadosJson(model.getDadosJson());
        return dto;
    }

    public static RelatorioModel toModel(RelatorioDTO dto) {
        if (dto == null) return null;
        RelatorioModel model = new RelatorioModel();
        model.setId(dto.getId());
        // condominio deve ser setado no service/controller
        model.setTipoRelatorio(dto.getTipoRelatorio());
        model.setPeriodoInicio(dto.getPeriodoInicio());
        model.setPeriodoFim(dto.getPeriodoFim());
        model.setDadosJson(dto.getDadosJson());
        return model;
    }
}
