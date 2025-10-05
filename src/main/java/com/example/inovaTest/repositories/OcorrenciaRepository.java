package com.example.inovaTest.repositories;

import com.example.inovaTest.models.OcorrenciaModel;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OcorrenciaRepository extends JpaRepository<OcorrenciaModel, Long> {

    // Encontra todas as ocorrências cujo status NÃO esteja na lista fornecida.
    // isso para filtrar as ocorrências "CANCELADAS" da visualização principal.
    List<OcorrenciaModel> findAllByStatusOcorrenciaNotIn(Collection<String> statuses);

    // Versão otimizada para buscar ocorrências de um morador específico,
    // também excluindo os status indesejados. Isso evita filtrar na memória.
    List<OcorrenciaModel> findByMoradorIdAndStatusOcorrenciaNotIn(Long moradorId, Collection<String> statuses);


    // Método para buscar ocorrências por status(canceladas, abertas, etc)
    List<OcorrenciaModel> findByStatusOcorrencia(String status);
}
