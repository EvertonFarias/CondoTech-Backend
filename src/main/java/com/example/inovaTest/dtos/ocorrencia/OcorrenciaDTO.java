package com.example.inovaTest.dtos.ocorrencia;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OcorrenciaDTO {
    private Long id;
    private Long moradorId;
    private String moradorNome; 
    private String tipoOcorrencia;
    private String titulo;
    private String descricao;
    private String statusOcorrencia;
    private String respostaSindico;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime dataResolucao;
    private LocalDateTime updatedAt;
}