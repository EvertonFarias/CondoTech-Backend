package com.example.inovaTest.controllers;

import com.example.inovaTest.models.ReservaModel;
import com.example.inovaTest.repositories.ReservaRepository;
import com.example.inovaTest.dtos.reserva.ReservaDTO;
import com.example.inovaTest.mappers.ReservaMapper;
import com.example.inovaTest.models.AreaComumModel;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.repositories.AreaComumRepository;
import com.example.inovaTest.repositories.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private ReservaRepository repository;

    @Autowired
    private AreaComumRepository areaComumRepository;

    @Autowired
    private MoradorRepository moradorRepository;


    @GetMapping
    public List<ReservaDTO> findAll() {
        return repository.findAll().stream().map(ReservaMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ReservaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ReservaDTO create(@RequestBody ReservaDTO dto) {
        ReservaModel model = ReservaMapper.toModel(dto);
        if (dto.getAreaComumId() != null) {
            AreaComumModel areaComum = areaComumRepository.findById(dto.getAreaComumId())
                .orElseThrow(() -> new RuntimeException("AreaComum não encontrada"));
            model.setAreaComum(areaComum);
        }
        if (dto.getMoradorId() != null) {
            MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
            model.setMorador(morador);
        }
        return ReservaMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> update(@PathVariable Long id, @RequestBody ReservaDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    ReservaModel model = ReservaMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getAreaComumId() != null) {
                        AreaComumModel areaComum = areaComumRepository.findById(dto.getAreaComumId())
                            .orElseThrow(() -> new RuntimeException("AreaComum não encontrada"));
                        model.setAreaComum(areaComum);
                    }
                    if (dto.getMoradorId() != null) {
                        MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
                        model.setMorador(morador);
                    }
                    return ResponseEntity.ok(ReservaMapper.toDTO(repository.save(model)));
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
