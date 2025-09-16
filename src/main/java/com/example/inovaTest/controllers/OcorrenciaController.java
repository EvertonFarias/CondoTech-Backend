package com.example.inovaTest.controllers;

import com.example.inovaTest.models.OcorrenciaModel;
import com.example.inovaTest.repositories.OcorrenciaRepository;
import com.example.inovaTest.dtos.ocorrencia.OcorrenciaDTO;
import com.example.inovaTest.mappers.OcorrenciaMapper;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.repositories.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {
    @Autowired
    private OcorrenciaRepository repository;

    @Autowired
    private MoradorRepository moradorRepository;


    @GetMapping
    public List<OcorrenciaDTO> findAll() {
        return repository.findAll().stream().map(OcorrenciaMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(OcorrenciaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public OcorrenciaDTO create(@RequestBody OcorrenciaDTO dto) {
        OcorrenciaModel model = OcorrenciaMapper.toModel(dto);
        if (dto.getMoradorId() != null) {
            MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
            model.setMorador(morador);
        }
        return OcorrenciaMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> update(@PathVariable Long id, @RequestBody OcorrenciaDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    OcorrenciaModel model = OcorrenciaMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getMoradorId() != null) {
                        MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
                        model.setMorador(morador);
                    }
                    return ResponseEntity.ok(OcorrenciaMapper.toDTO(repository.save(model)));
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
