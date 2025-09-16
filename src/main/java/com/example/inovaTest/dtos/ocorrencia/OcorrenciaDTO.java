package com.example.inovaTest.dtos.ocorrencia;

import lombok.Data;

@Data
public class OcorrenciaDTO {
    private Long id;
    private Long moradorId;
    private String tipoOcorrencia;
    private String titulo;
    private String descricao;
    private String statusOcorrencia;
    private String respostaSindico;
}
