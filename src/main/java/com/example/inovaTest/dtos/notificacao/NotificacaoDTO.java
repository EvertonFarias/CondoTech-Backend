package com.example.inovaTest.dtos.notificacao;

import lombok.Data;

@Data
public class NotificacaoDTO {
    private Long id;
    private Long moradorId;
    private String titulo;
    private String mensagem;
    private Boolean lida;
}
