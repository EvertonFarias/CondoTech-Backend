package com.example.inovaTest.dtos.areaComum;

import com.example.inovaTest.models.AreaComumFotoModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FotoAreaComumResponseDTO {
    private Long id;
    private String url;
    private String nomeArquivo;
    private Integer ordem;
    private Boolean principal;
    private LocalDateTime createdAt;

    public FotoAreaComumResponseDTO(AreaComumFotoModel model) {
        this.id = model.getId();
        this.url = model.getUrl();
        this.nomeArquivo = model.getNomeArquivo();
        this.ordem = model.getOrdem();
        this.principal = model.getPrincipal();
        this.createdAt = model.getCreatedAt();
    }
}