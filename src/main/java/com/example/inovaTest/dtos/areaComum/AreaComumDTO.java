package com.example.inovaTest.dtos.areaComum;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AreaComumDTO {
    private Long id;
    private Long condominioId;
    private String nome;
    private String descricao;
    private Integer capacidadeMaxima;
    private BigDecimal valorTaxa;
    private Boolean ativa;
}
