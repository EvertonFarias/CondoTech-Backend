package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regras")
@EqualsAndHashCode(of = "id") 
public class RegraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condominio", nullable = false)
    private CondominioModel condominio;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O título da regra é obrigatório")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    
    @ManyToMany(mappedBy = "regras")
    private Set<AreaComumModel> areasComuns;
}