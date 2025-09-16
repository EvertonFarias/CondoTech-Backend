package com.example.inovaTest.dtos.unidade;

import lombok.Data;

@Data
public class UnidadeDTO {
    private Long id;
    private Long condominioId;
    private String numero;
    private String bloco;
    private Integer andar;
    private String tipoUnidade;
    private Boolean ocupada;
}
