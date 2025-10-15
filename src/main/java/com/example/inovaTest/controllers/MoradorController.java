package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.morador.MoradorDTO;
import com.example.inovaTest.mappers.MoradorMapper;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.models.UnidadeModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moradores")
@CrossOrigin(origins = "*") // Lembre-se de restringir em produção para mais segurança
public class MoradorController {

    // Injeção de dependências (sem alterações)
    @Autowired private MoradorRepository repository;
    @Autowired private UnidadeRepository unidadeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private NotificacaoRepository notificacaoRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private OcorrenciaRepository ocorrenciaRepository;

    @GetMapping("/{moradorId}/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticas(@PathVariable Long moradorId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. Notificações não lidas
        long unreadNotifications = notificacaoRepository.countByMoradorIdAndLidaFalse(moradorId);
        stats.put("notificacoesNaoLidas", unreadNotifications);
        
        // 2. Reservas próximas (a partir de hoje)
        LocalDate hoje = LocalDate.now();
        long upcomingReservations = reservaRepository.countByMoradorIdAndDataReservaGreaterThanEqual(moradorId, hoje);
        stats.put("reservasProximas", upcomingReservations);
        
        // 3. Ocorrências em aberto
        long openIssues = ocorrenciaRepository.countByMoradorIdAndStatusOcorrenciaIn(
                moradorId, 
                List.of("ABERTA", "EM_ANDAMENTO")
        );
        stats.put("ocorrenciasAbertas", openIssues);
        
        return ResponseEntity.ok(stats);
    }

    // Endpoint para contar o total de moradores (sem alterações)
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countMoradores() {
        long count = repository.count();
        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        return ResponseEntity.ok(response);
    }

    // --- Outros endpoints do CRUD de moradores (sem alterações) ---

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
        // Lógica para associar unidade e usuário (sem alterações)
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
                    // Lógica para associar unidade e usuário (sem alterações)
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