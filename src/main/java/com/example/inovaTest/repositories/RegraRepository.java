package com.example.inovaTest.repositories;

import com.example.inovaTest.models.RegraModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; 

@Repository
public interface RegraRepository extends JpaRepository<RegraModel, Long> {

    //Encontra todas as regras para um determinado condomínio
    List<RegraModel> findAllByCondominioId(Long condominioId);
}