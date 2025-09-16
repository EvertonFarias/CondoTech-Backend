package com.example.inovaTest.controllers;

import com.example.inovaTest.models.AreaComumModel;
import com.example.inovaTest.repositories.AreaComumRepository;
import com.example.inovaTest.dtos.areaComum.AreaComumDTO;
import com.example.inovaTest.mappers.AreaComumMapper;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.repositories.CondominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas-comuns")
public class AreaComumController {
    @Autowired
    private AreaComumRepository repository;

    @Autowired
    private CondominioRepository condominioRepository;


    @GetMapping
    public List<AreaComumDTO> findAll() {
        return repository.findAll().stream().map(AreaComumMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<AreaComumDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(AreaComumMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public AreaComumDTO create(@RequestBody AreaComumDTO dto) {
        AreaComumModel model = AreaComumMapper.toModel(dto);
        if (dto.getCondominioId() != null) {
            CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
            model.setCondominio(condominio);
        }
        return AreaComumMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AreaComumDTO> update(@PathVariable Long id, @RequestBody AreaComumDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    AreaComumModel model = AreaComumMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getCondominioId() != null) {
                        CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                            .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
                        model.setCondominio(condominio);
                    }
                    return ResponseEntity.ok(AreaComumMapper.toDTO(repository.save(model)));
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
