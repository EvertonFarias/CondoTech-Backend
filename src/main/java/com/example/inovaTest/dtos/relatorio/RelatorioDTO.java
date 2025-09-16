package com.example.inovaTest.dtos.relatorio;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RelatorioDTO {
    private Long id;
    private Long condominioId;
    private String tipoRelatorio;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
    private String dadosJson;
}
