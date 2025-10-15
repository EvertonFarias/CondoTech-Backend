package com.example.inovaTest.repositories;

import com.example.inovaTest.models.NotificacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<NotificacaoModel, Long> {
    
    // Buscar notificações de um morador específico, ordenadas por data
    List<NotificacaoModel> findByMoradorIdOrderByCreatedAtDesc(Long moradorId);
    
    // Buscar as 3 notificações mais recentes de um morador
    List<NotificacaoModel> findTop3ByMoradorIdOrderByCreatedAtDesc(Long moradorId);
    
    // Contar notificações não lidas de um morador
    long countByMoradorIdAndLidaFalse(Long moradorId);
}