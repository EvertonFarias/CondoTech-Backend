
package com.example.inovaTest.controllers;

import com.example.inovaTest.mappers.SindicoMapper;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.repositories.CondominioRepository;
import com.example.inovaTest.models.SindicoModel;
import com.example.inovaTest.repositories.SindicoRepository;
import com.example.inovaTest.dtos.sindico.SindicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sindicos")
public class SindicoController {
    @Autowired
    private SindicoRepository repository;

    @Autowired
    private CondominioRepository condominioRepository;





    @GetMapping
    public List<SindicoDTO> findAll() {
        return repository.findAll().stream().map(SindicoMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SindicoDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(SindicoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SindicoDTO create(@RequestBody SindicoDTO dto) {
        SindicoModel model = SindicoMapper.toModel(dto);
        if (dto.getCondominioId() != null) {
            CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
            model.setCondominio(condominio);
        }
        return SindicoMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SindicoDTO> update(@PathVariable Long id, @RequestBody SindicoDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    SindicoModel model = SindicoMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getCondominioId() != null) {
                        CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                            .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
                        model.setCondominio(condominio);
                    }
                    return ResponseEntity.ok(SindicoMapper.toDTO(repository.save(model)));
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
