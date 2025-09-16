package com.example.inovaTest.controllers;

import com.example.inovaTest.models.UnidadeModel;
import com.example.inovaTest.repositories.UnidadeRepository;
import com.example.inovaTest.dtos.unidade.UnidadeDTO;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.repositories.CondominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {
    @Autowired
    private UnidadeRepository repository;

    @Autowired
    private CondominioRepository condominioRepository;


    @GetMapping
    public List<UnidadeDTO> findAll() {
        return repository.findAll().stream().map(model -> {
            UnidadeDTO dto = new UnidadeDTO();
            dto.setId(model.getId());
            dto.setCondominioId(model.getCondominio() != null ? model.getCondominio().getId() : null);
            dto.setNumero(model.getNumero());
            dto.setBloco(model.getBloco());
            dto.setAndar(model.getAndar());
            dto.setTipoUnidade(model.getTipoUnidade());
            dto.setOcupada(model.getOcupada());
            return dto;
        }).toList();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UnidadeDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(model -> {
                    UnidadeDTO dto = new UnidadeDTO();
                    dto.setId(model.getId());
                    dto.setCondominioId(model.getCondominio() != null ? model.getCondominio().getId() : null);
                    dto.setNumero(model.getNumero());
                    dto.setBloco(model.getBloco());
                    dto.setAndar(model.getAndar());
                    dto.setTipoUnidade(model.getTipoUnidade());
                    dto.setOcupada(model.getOcupada());
                    return dto;
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public UnidadeDTO create(@RequestBody UnidadeDTO dto) {
        UnidadeModel model = new UnidadeModel();
        model.setId(dto.getId());
        if (dto.getCondominioId() != null) {
            CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
            model.setCondominio(condominio);
        }
        model.setNumero(dto.getNumero());
        model.setBloco(dto.getBloco());
        model.setAndar(dto.getAndar());
        model.setTipoUnidade(dto.getTipoUnidade());
        model.setOcupada(dto.getOcupada());
        UnidadeModel saved = repository.save(model);
        UnidadeDTO result = new UnidadeDTO();
        result.setId(saved.getId());
        result.setCondominioId(saved.getCondominio() != null ? saved.getCondominio().getId() : null);
        result.setNumero(saved.getNumero());
        result.setBloco(saved.getBloco());
        result.setAndar(saved.getAndar());
        result.setTipoUnidade(saved.getTipoUnidade());
        result.setOcupada(saved.getOcupada());
        return result;
    }


    @PutMapping("/{id}")
    public ResponseEntity<UnidadeDTO> update(@PathVariable Long id, @RequestBody UnidadeDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    UnidadeModel model = new UnidadeModel();
                    model.setId(id);
                    if (dto.getCondominioId() != null) {
                        CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                            .orElseThrow(() -> new RuntimeException("Condomínio não encontrado"));
                        model.setCondominio(condominio);
                    }
                    model.setNumero(dto.getNumero());
                    model.setBloco(dto.getBloco());
                    model.setAndar(dto.getAndar());
                    model.setTipoUnidade(dto.getTipoUnidade());
                    model.setOcupada(dto.getOcupada());
                    UnidadeModel saved = repository.save(model);
                    UnidadeDTO result = new UnidadeDTO();
                    result.setId(saved.getId());
                    result.setCondominioId(saved.getCondominio() != null ? saved.getCondominio().getId() : null);
                    result.setNumero(saved.getNumero());
                    result.setBloco(saved.getBloco());
                    result.setAndar(saved.getAndar());
                    result.setTipoUnidade(saved.getTipoUnidade());
                    result.setOcupada(saved.getOcupada());
                    return ResponseEntity.ok(result);
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
