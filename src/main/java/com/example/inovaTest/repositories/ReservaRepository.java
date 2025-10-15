package com.example.inovaTest.repositories;

import com.example.inovaTest.models.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<ReservaModel, Long> {

    // Busca todas as reservas para uma área específica em um dia específico
    // Usaremos isso para verificar a disponibilidade.
    List<ReservaModel> findByAreaComumIdAndDataReserva(Long areaComumId, LocalDate dataReserva);

    // Busca todas as reservas de um usuário (através do ID do UserModel)
    // Usaremos isso para a tela "Minhas Reservas".
    List<ReservaModel> findByMoradorUsuarioId(UUID usuarioId);
}