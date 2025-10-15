package com.example.inovaTest.dtos.areaComum;

import com.example.inovaTest.dtos.regras.RegraDTO;
import com.example.inovaTest.enums.TipoReserva;
import com.example.inovaTest.models.AreaComumModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AreaComumDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String icone;
    private boolean ativa;
    private BigDecimal valorTaxa;
    private Set<TipoReserva> tiposReservaDisponiveis;
    private Long condominioId;
    private String condominioNome;
    private List<FotoAreaComumResponseDTO> fotos;

    // ALTERADO: Agora retornamos uma lista de objetos RegraDTO
    private Set<RegraDTO> regras;

    public AreaComumDTO(AreaComumModel model) {
        this.id = model.getId();
        this.nome = model.getNome();
        this.descricao = model.getDescricao();
        this.icone = model.getIcone();
        this.ativa = model.isAtiva();
        this.valorTaxa = model.getValorTaxa();
        this.tiposReservaDisponiveis = model.getTiposReservaDisponiveis();
        this.condominioId = model.getCondominio().getId();
        this.condominioNome = model.getCondominio().getNome();
        
        // Mapeia as regras para DTOs
        if (model.getRegras() != null) {
            this.regras = model.getRegras().stream()
                .map(RegraDTO::new)
                .collect(Collectors.toSet());
        }
        
        // Converte fotos
        if (model.getFotos() != null) {
            this.fotos = model.getFotos().stream()
                .map(FotoAreaComumResponseDTO::new)
                .collect(Collectors.toList());
        }
    }
}