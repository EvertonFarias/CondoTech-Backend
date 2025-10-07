package com.example.inovaTest.dtos.comentario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {
    private Long id;
    private Long ocorrenciaId;
    private Long moradorId;
    private String texto;
    private Boolean isAdmin;
    private String moradorNome;
    private LocalDateTime createdAt;
}