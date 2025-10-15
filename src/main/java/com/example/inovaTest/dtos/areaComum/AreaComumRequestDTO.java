package com.example.inovaTest.dtos.areaComum;

import com.example.inovaTest.enums.TipoReserva;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class AreaComumRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;
    
    private String icone;

    @NotNull(message = "Status de ativação é obrigatório")
    private Boolean ativa;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal valorTaxa;

    private Set<Long> regraIds; 
    
    @NotEmpty(message = "Pelo menos um tipo de reserva deve ser selecionado")
    private Set<TipoReserva> tiposReservaDisponiveis;
    
    @NotNull(message = "ID do condomínio é obrigatório")
    private Long condominioId;
}