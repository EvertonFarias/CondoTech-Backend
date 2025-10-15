package com.example.inovaTest.dtos.regras;

import com.example.inovaTest.models.RegraModel;
import lombok.Data;

@Data
public class RegraDTO {
    private Long id;
    private String titulo;
    private String descricao;

    public RegraDTO(RegraModel model) {
        this.id = model.getId();
        this.titulo = model.getTitulo();
        this.descricao = model.getDescricao();
    }
}