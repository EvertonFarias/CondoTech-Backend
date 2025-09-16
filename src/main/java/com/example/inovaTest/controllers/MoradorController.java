package com.example.inovaTest.controllers;

import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.repositories.MoradorRepository;
import com.example.inovaTest.dtos.morador.MoradorDTO;
import com.example.inovaTest.mappers.MoradorMapper;
import com.example.inovaTest.models.UnidadeModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UnidadeRepository;
import com.example.inovaTest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moradores")
public class MoradorController {
    @Autowired
    private MoradorRepository repository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public List<MoradorDTO> findAll() {
        return repository.findAll().stream().map(MoradorMapper::toDTO).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<MoradorDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(MoradorMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public MoradorDTO create(@RequestBody MoradorDTO dto) {
        MoradorModel model = MoradorMapper.toModel(dto);
        if (dto.getUnidadeId() != null) {
            UnidadeModel unidade = unidadeRepository.findById(dto.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
            model.setUnidade(unidade);
        }
        if (dto.getUsuarioId() != null) {
            UserModel usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            model.setUsuario(usuario);
        }
        return MoradorMapper.toDTO(repository.save(model));
    }


    @PutMapping("/{id}")
    public ResponseEntity<MoradorDTO> update(@PathVariable Long id, @RequestBody MoradorDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    MoradorModel model = MoradorMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getUnidadeId() != null) {
                        UnidadeModel unidade = unidadeRepository.findById(dto.getUnidadeId())
                            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
                        model.setUnidade(unidade);
                    }
                    if (dto.getUsuarioId() != null) {
                        UserModel usuario = userRepository.findById(dto.getUsuarioId())
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                        model.setUsuario(usuario);
                    }
                    return ResponseEntity.ok(MoradorMapper.toDTO(repository.save(model)));
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
