package com.example.inovaTest.models;

import com.example.inovaTest.enums.TipoReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "areas_comuns")
public class AreaComumModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condominio", nullable = false)
    private CondominioModel condominio;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(length = 50)
    private String icone;

    @Column(nullable = false)
    private boolean ativa = true;

    @Column(name = "valor_taxa", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal valorTaxa;

   
    // relação N-para-N
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "area_comum_regras_associacao", // Nome da tabela de junção
        joinColumns = @JoinColumn(name = "area_comum_id"),
        inverseJoinColumns = @JoinColumn(name = "regra_id")
    )
    private Set<RegraModel> regras = new HashSet<>();
    
    // Lista de tipos de reserva permitidos (POR_HORA, DIARIA, etc.)
    @ElementCollection(targetClass = TipoReserva.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "area_comum_tipos_reserva", joinColumns = @JoinColumn(name = "area_comum_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reserva", nullable = false)
    private Set<TipoReserva> tiposReservaDisponiveis;

    @OneToMany(mappedBy = "areaComum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservaModel> reservas;
    
    @OneToMany(mappedBy = "areaComum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AreaComumFotoModel> fotos;
}