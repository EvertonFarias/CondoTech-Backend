package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ocorrencias")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OcorrenciaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_morador", nullable = false)
    private MoradorModel morador;

    @Column(name = "tipo_ocorrencia", length = 20)
    @Size(max = 20, message = "Tipo de ocorrência deve ter no máximo 20 caracteres")
    private String tipoOcorrencia; // RECLAMACAO, SUGESTAO, MANUTENCAO, EMERGENCIA

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "status_ocorrencia", length = 20, nullable = false)
    @Size(max = 20, message = "Status da ocorrência deve ter no máximo 20 caracteres")
    private String statusOcorrencia = "ABERTA"; // ABERTA, EM_ANDAMENTO, RESOLVIDA, FECHADA

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Column(name = "resposta_sindico", columnDefinition = "TEXT")
    private String respostaSindico;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "image_url")
    private String imageUrl;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}