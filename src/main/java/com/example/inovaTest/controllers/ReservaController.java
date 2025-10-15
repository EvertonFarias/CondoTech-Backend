package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.areaComum.AreaComumDTO;
import com.example.inovaTest.dtos.reserva.CreateReservaRequestDTO;
import com.example.inovaTest.dtos.reserva.ReservaDTO;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/areas")
    public ResponseEntity<List<AreaComumDTO>> getAreasComuns() {
        return ResponseEntity.ok(reservaService.getAllAreasComuns());
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<ReservaDTO>> getMinhasReservas(Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        return ResponseEntity.ok(reservaService.getMinhasReservas(user));
    }
    
    @PostMapping
    public ResponseEntity<ReservaDTO> createReserva(@RequestBody @Valid CreateReservaRequestDTO request, Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        ReservaDTO novaReserva = reservaService.createReserva(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id, Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        reservaService.cancelarReserva(id, user);
        return ResponseEntity.noContent().build();
    }
}