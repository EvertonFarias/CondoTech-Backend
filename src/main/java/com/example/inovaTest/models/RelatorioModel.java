package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relatorios")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RelatorioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condominio", nullable = false)
    private CondominioModel condominio;

    @Column(name = "tipo_relatorio", length = 20)
    @Size(max = 20, message = "Tipo de relatório deve ter no máximo 20 caracteres")
    private String tipoRelatorio; // MENSAL, TRIMESTRAL, ANUAL, PERSONALIZADO

    @Column(name = "periodo_inicio", nullable = false)
    @NotNull(message = "Período de início é obrigatório")
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    @NotNull(message = "Período de fim é obrigatório")
    private LocalDate periodoFim;

    @Column(name = "dados_json", columnDefinition = "TEXT")
    private String dadosJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}