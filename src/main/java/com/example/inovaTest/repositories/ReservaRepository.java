package com.example.inovaTest.repositories;

import com.example.inovaTest.models.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaModel, Long> {
    
    List<ReservaModel> findByDataReservaGreaterThanEqualOrderByDataReservaAsc(LocalDate data);
    
    // Contar reservas futuras de um morador
    long countByMoradorIdAndDataReservaGreaterThanEqual(Long moradorId, LocalDate data);
    
    // Buscar reservas próximas de um morador específico
    List<ReservaModel> findByMoradorIdAndDataReservaGreaterThanEqualOrderByDataReservaAsc(
        Long moradorId, 
        LocalDate data
    );
}