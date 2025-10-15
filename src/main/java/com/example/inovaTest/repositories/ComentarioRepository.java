package com.example.inovaTest.repositories;

import com.example.inovaTest.models.ComentarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioModel, Long> {
    
    /**
     * Busca todos os comentários de uma ocorrência específica, ordenados por data de criação (mais antigos primeiro)
     * @param ocorrenciaId ID da ocorrência
     * @return Lista de comentários ordenados cronologicamente
     */
    List<ComentarioModel> findByOcorrenciaIdOrderByCreatedAtAsc(Long ocorrenciaId);
    
    /**
     * Busca todos os comentários de um morador específico
     * @param moradorId ID do morador
     * @return Lista de comentários do morador
     */
    List<ComentarioModel> findByMoradorId(Long moradorId);
    
    /**
     * Conta quantos comentários uma ocorrência possui
     * @param ocorrenciaId ID da ocorrência
     * @return Número de comentários
     */
    Long countByOcorrenciaId(Long ocorrenciaId);
    
    /**
     * Busca comentários de admin em uma ocorrência específica
     * @param ocorrenciaId ID da ocorrência
     * @param isAdmin true para buscar apenas comentários de admin
     * @return Lista de comentários filtrados
     */
    List<ComentarioModel> findByOcorrenciaIdAndIsAdminOrderByCreatedAtAsc(Long ocorrenciaId, Boolean isAdmin);
}