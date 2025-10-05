package com.example.inovaTest.mappers;

import com.example.inovaTest.dtos.comentario.ComentarioDTO;
import com.example.inovaTest.models.ComentarioModel;

public class ComentarioMapper {
    
    public static ComentarioDTO toDTO(ComentarioModel model) {
        if (model == null) return null;
        
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(model.getId());
        dto.setOcorrenciaId(model.getOcorrencia() != null ? model.getOcorrencia().getId() : null);
        dto.setMoradorId(model.getMorador() != null ? model.getMorador().getId() : null);
        dto.setTexto(model.getTexto());
        dto.setIsAdmin(model.getIsAdmin());
        dto.setCreatedAt(model.getCreatedAt());
        
        // Define o nome do morador
        if (model.getMorador() != null) {
            dto.setMoradorNome(model.getMorador().getNome());
        }
        
        return dto;
    }
    
    public static ComentarioModel toModel(ComentarioDTO dto) {
        if (dto == null) return null;
        
        ComentarioModel model = new ComentarioModel();
        model.setId(dto.getId());
        model.setTexto(dto.getTexto());
        model.setIsAdmin(dto.getIsAdmin() != null ? dto.getIsAdmin() : false);
        
        return model;
    }
}