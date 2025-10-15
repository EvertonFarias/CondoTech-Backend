package com.example.inovaTest.dtos.morador;

import lombok.Data;
import java.util.UUID;

import com.example.inovaTest.enums.TipoMorador;

@Data
public class MoradorDTO {
    private Long id;
    private Long unidadeId;
    private UUID usuarioId;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private TipoMorador tipoMorador;
    private Boolean ativo;
}
