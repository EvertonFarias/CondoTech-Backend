package com.example.inovaTest.repositories;

import com.example.inovaTest.models.OcorrenciaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OcorrenciaRepository extends JpaRepository<OcorrenciaModel, Long> {

    @Query("SELECT o FROM OcorrenciaModel o WHERE " +
           "(:query IS NULL OR :query = '' OR LOWER(o.titulo) LIKE :query OR LOWER(o.descricao) LIKE :query OR LOWER(o.morador.nome) LIKE :query) AND " +
           "(:status IS NULL OR :status = '' OR o.statusOcorrencia = :status) AND " +
           "(:tipo IS NULL OR :tipo = '' OR o.tipoOcorrencia = :tipo)")
    Page<OcorrenciaModel> searchAll(
        @Param("query") String query, 
        @Param("status") String status, 
        @Param("tipo") String tipo, 
        Pageable pageable
    );

    
    @Query("SELECT o FROM OcorrenciaModel o WHERE " +
           "o.morador.id = :moradorId AND " +
           "(:query IS NULL OR :query = '' OR LOWER(o.titulo) LIKE :query OR LOWER(o.descricao) LIKE :query) AND " +
           "(:status IS NULL OR :status = '' OR o.statusOcorrencia = :status) AND " +
           "(:tipo IS NULL OR :tipo = '' OR o.tipoOcorrencia = :tipo)")
    Page<OcorrenciaModel> searchByMorador(
        @Param("moradorId") Long moradorId,
        @Param("query") String query, 
        @Param("status") String status, 
        @Param("tipo") String tipo, 
        Pageable pageable
    );
}