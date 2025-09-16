package com.example.inovaTest.repositories;

import com.example.inovaTest.models.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<ReservaModel, Long> {
}
