package com.example.inovaTest.dtos.regras;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegraRequestDTO {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;
    private String descricao;
    @NotNull(message = "ID do condomínio é obrigatório")
    private Long condominioId;
}