package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "analises_ia")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AnaliseIaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_morador", nullable = false)
    private MoradorModel morador;

    @Column(name = "tipo_comportamento", length = 20)
    @Size(max = 20, message = "Tipo de comportamento deve ter no máximo 20 caracteres")
    private String tipoComportamento; // POSITIVO, NEUTRO, NEGATIVO, SUSPEITO

    @Column(name = "score_comportamento")
    @Min(value = 0, message = "Score deve ser entre 0 e 100")
    @Max(value = 100, message = "Score deve ser entre 0 e 100")
    private Integer scoreComportamento;

    @Column(name = "nivel_alerta", length = 20)
    @Size(max = 20, message = "Nível de alerta deve ter no máximo 20 caracteres")
    private String nivelAlerta; // BAIXO, MEDIO, ALTO, CRITICO

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "requer_acao", nullable = false)
    private Boolean requerAcao = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}