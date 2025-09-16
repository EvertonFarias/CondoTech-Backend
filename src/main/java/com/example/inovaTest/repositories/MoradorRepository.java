package com.example.inovaTest.repositories;

import com.example.inovaTest.models.MoradorModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoradorRepository extends JpaRepository<MoradorModel, Long> {
}
