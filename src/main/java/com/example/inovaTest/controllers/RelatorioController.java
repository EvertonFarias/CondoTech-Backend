package com.example.inovaTest.controllers;

import com.example.inovaTest.models.RelatorioModel;
import com.example.inovaTest.repositories.RelatorioRepository;
import com.example.inovaTest.dtos.relatorio.RelatorioDTO;
import com.example.inovaTest.mappers.RelatorioMapper;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.repositories.CondominioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {
    @Autowired
    private RelatorioRepository repository;

    @Autowired
    private CondominioRepository condominioRepository;




    @GetMapping
    public List<RelatorioDTO> findAll() {
        return repository.findAll().stream().map(RelatorioMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<RelatorioDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(RelatorioMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public RelatorioDTO create(@RequestBody RelatorioDTO dto) {
        RelatorioModel model = RelatorioMapper.toModel(dto);
        if (dto.getCondominioId() != null) {
            CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
            model.setCondominio(condominio);
        }
        return RelatorioMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<RelatorioDTO> update(@PathVariable Long id, @RequestBody RelatorioDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    RelatorioModel model = RelatorioMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getCondominioId() != null) {
                        CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                            .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
                        model.setCondominio(condominio);
                    }
                    return ResponseEntity.ok(RelatorioMapper.toDTO(repository.save(model)));
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
