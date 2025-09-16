package com.example.inovaTest.dtos.condominio;

import lombok.Data;

@Data
public class CondominioDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String cnpj;
}
