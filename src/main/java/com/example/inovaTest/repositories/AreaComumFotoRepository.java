package com.example.inovaTest.repositories;

import com.example.inovaTest.models.AreaComumFotoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaComumFotoRepository extends JpaRepository<AreaComumFotoModel, Long> {
    
    List<AreaComumFotoModel> findByAreaComumIdOrderByOrdemAsc(Long areaComumId);
    
    Long countByAreaComumId(Long areaComumId);
    
    Optional<AreaComumFotoModel> findByAreaComumIdAndPrincipalTrue(Long areaComumId);
    
    @Query("SELECT f FROM AreaComumFotoModel f WHERE f.areaComum.id = :areaComumId AND f.id = :fotoId")
    Optional<AreaComumFotoModel> findByAreaComumIdAndId(Long areaComumId, Long fotoId);
}