package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "areas_comuns")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AreaComumModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condominio", nullable = false)
    private CondominioModel condominio;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "capacidade_maxima")
    private Integer capacidadeMaxima;

    @Column(name = "valor_taxa", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true, message = "Valor da taxa deve ser positivo")
    private BigDecimal valorTaxa;

    @Column(nullable = false)
    private Boolean ativa = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relacionamentos
    @OneToMany(mappedBy = "areaComum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservaModel> reservas;
}