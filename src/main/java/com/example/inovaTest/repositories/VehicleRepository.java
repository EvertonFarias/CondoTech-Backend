package com.example.inovaTest.repositories;

import com.example.inovaTest.models.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleModel, Long> {
}