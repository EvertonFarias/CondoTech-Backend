package com.example.inovaTest.dtos.sindico;

import lombok.Data;
import java.util.UUID;

@Data
public class SindicoDTO {
    private Long id;
    private Long condominioId;
    private UUID usuarioId;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private Boolean ativo;
}
