package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comentarios_ocorrencia")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ComentarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ocorrencia", nullable = false)
    private OcorrenciaModel ocorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_morador", nullable = false)
    private MoradorModel morador;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Comentário não pode estar vazio")
    @Size(max = 1000, message = "Comentário deve ter no máximo 1000 caracteres")
    private String texto;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false; // Identifica se o morador é admin/síndico

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}