package com.example.inovaTest.controllers;

import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.repositories.CondominioRepository;
import com.example.inovaTest.dtos.condominio.CondominioDTO;
import com.example.inovaTest.mappers.CondominioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/condominios")
public class CondominioController {
    @Autowired
    private CondominioRepository repository;


    @GetMapping
    public List<CondominioDTO> findAll() {
        return repository.findAll().stream().map(CondominioMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CondominioDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(CondominioMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public CondominioDTO create(@RequestBody CondominioDTO dto) {
        CondominioModel model = CondominioMapper.toModel(dto);
        return CondominioMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CondominioDTO> update(@PathVariable Long id, @RequestBody CondominioDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    CondominioModel model = CondominioMapper.toModel(dto);
                    model.setId(id);
                    return ResponseEntity.ok(CondominioMapper.toDTO(repository.save(model)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
