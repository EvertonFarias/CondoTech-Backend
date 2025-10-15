package com.example.inovaTest.controllers;

import com.example.inovaTest.models.NotificacaoModel;
import com.example.inovaTest.repositories.NotificacaoRepository;
import com.example.inovaTest.dtos.notificacao.NotificacaoDTO;
import com.example.inovaTest.mappers.NotificacaoMapper;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.repositories.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "*")
public class NotificacaoController {
    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private MoradorRepository moradorRepository;

    @GetMapping
    public List<NotificacaoDTO> findAll() {
        return repository.findAll().stream().map(NotificacaoMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(NotificacaoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar notificações de um morador específico
    @GetMapping("/morador/{moradorId}")
    public List<NotificacaoDTO> findByMorador(@PathVariable Long moradorId) {
        return repository.findByMoradorIdOrderByCreatedAtDesc(moradorId)
                .stream()
                .map(NotificacaoMapper::toDTO)
                .toList();
    }

    // Endpoint para buscar notificações recentes (últimas 3)
    @GetMapping("/morador/{moradorId}/recentes")
    public List<NotificacaoDTO> findRecentByMorador(@PathVariable Long moradorId) {
        return repository.findTop3ByMoradorIdOrderByCreatedAtDesc(moradorId)
                .stream()
                .map(NotificacaoMapper::toDTO)
                .toList();
    }

    // Endpoint para contar notificações não lidas
    @GetMapping("/morador/{moradorId}/nao-lidas/count")
    public ResponseEntity<Map<String, Long>> countUnreadByMorador(@PathVariable Long moradorId) {
        long count = repository.countByMoradorIdAndLidaFalse(moradorId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // Endpoint para marcar notificação como lida
    @PatchMapping("/{id}/marcar-lida")
    public ResponseEntity<NotificacaoDTO> markAsRead(@PathVariable Long id) {
        return repository.findById(id)
                .map(notificacao -> {
                    notificacao.setLida(true);
                    return ResponseEntity.ok(NotificacaoMapper.toDTO(repository.save(notificacao)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NotificacaoDTO create(@RequestBody NotificacaoDTO dto) {
        NotificacaoModel model = NotificacaoMapper.toModel(dto);
        if (dto.getMoradorId() != null) {
            MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
            model.setMorador(morador);
        }
        return NotificacaoMapper.toDTO(repository.save(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacaoDTO> update(@PathVariable Long id, @RequestBody NotificacaoDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    NotificacaoModel model = NotificacaoMapper.toModel(dto);
                    model.setId(id);
                    if (dto.getMoradorId() != null) {
                        MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
                        model.setMorador(morador);
                    }
                    return ResponseEntity.ok(NotificacaoMapper.toDTO(repository.save(model)));
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