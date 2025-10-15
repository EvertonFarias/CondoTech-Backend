package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.regras.RegraDTO;
import com.example.inovaTest.dtos.regras.RegraRequestDTO;
import com.example.inovaTest.services.RegraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/regras")
@PreAuthorize("hasRole('ADMIN')")
public class RegraController {

    @Autowired
    private RegraService regraService;

    @PostMapping
    public ResponseEntity<RegraDTO> create(@RequestBody @Valid RegraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regraService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<RegraDTO>> findAll() {
        return ResponseEntity.ok(regraService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegraDTO> update(@PathVariable Long id, @RequestBody @Valid RegraRequestDTO dto) {
        return ResponseEntity.ok(regraService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}