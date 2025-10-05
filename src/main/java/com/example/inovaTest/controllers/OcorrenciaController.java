package com.example.inovaTest.controllers;

import com.example.inovaTest.models.OcorrenciaModel;
import com.example.inovaTest.repositories.OcorrenciaRepository;
import com.example.inovaTest.services.FileStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.inovaTest.dtos.ocorrencia.OcorrenciaDTO;
import com.example.inovaTest.mappers.OcorrenciaMapper;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.repositories.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ocorrencias")
@CrossOrigin(origins = "*")
public class OcorrenciaController {
    @Autowired
    private OcorrenciaRepository repository;

    @Autowired
    private MoradorRepository moradorRepository;
    
    @Autowired
    private FileStorageService fileStorageService;

    // ✅ ALTERADO: O endpoint agora aceita um parâmetro opcional `status`.
    @GetMapping
    public List<OcorrenciaDTO> findAll(@RequestParam(required = false) String status) {
        List<OcorrenciaModel> ocorrencias;

        if (status != null && !status.isEmpty()) {
            // Se um status específico é solicitado (ex: "CANCELADA")
            if ("TODAS".equalsIgnoreCase(status)) {
                ocorrencias = repository.findAll();
            } else {
                ocorrencias = repository.findByStatusOcorrencia(status);
            }
        } else {
            // Comportamento padrão: retorna todas, exceto as canceladas.
            List<String> excludedStatuses = List.of("CANCELADA");
            ocorrencias = repository.findAllByStatusOcorrenciaNotIn(excludedStatuses);
        }

        return ocorrencias.stream()
                .map(OcorrenciaMapper::toDTO)
                .toList();
    }
    
    // ... (findById e findByMorador continuam os mesmos por enquanto)
    
    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(OcorrenciaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/morador/{moradorId}")
    public List<OcorrenciaDTO> findByMorador(@PathVariable Long moradorId) {
        List<String> excludedStatuses = List.of("CANCELADA");
        return repository.findByMoradorIdAndStatusOcorrenciaNotIn(moradorId, excludedStatuses).stream()
                .map(OcorrenciaMapper::toDTO)
                .toList();
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<OcorrenciaDTO> create(
        @RequestPart("ocorrencia") String ocorrenciaJson,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        // ... (código inalterado)
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OcorrenciaDTO dto = objectMapper.readValue(ocorrenciaJson, OcorrenciaDTO.class);
            OcorrenciaModel model = OcorrenciaMapper.toModel(dto);
            if (model.getStatusOcorrencia() == null || model.getStatusOcorrencia().isEmpty()) {
                model.setStatusOcorrencia("ABERTA");
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(imageFile);
                model.setImageUrl(imageUrl);
            }
            if (dto.getMoradorId() != null) {
                MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                        .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
                model.setMorador(morador);
            } else {
                return ResponseEntity.badRequest().build();
            }
            OcorrenciaModel saved = repository.save(model);
            return ResponseEntity.ok(OcorrenciaMapper.toDTO(saved));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar ocorrência: " + e.getMessage(), e);
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<OcorrenciaDTO> update(
        @PathVariable Long id,
        @RequestPart("ocorrencia") String ocorrenciaJson,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        // ... (código inalterado)
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OcorrenciaDTO dto = objectMapper.readValue(ocorrenciaJson, OcorrenciaDTO.class);
            return repository.findById(id)
                .map(existing -> {
                    existing.setTipoOcorrencia(dto.getTipoOcorrencia());
                    existing.setTitulo(dto.getTitulo());
                    existing.setDescricao(dto.getDescricao());
                    existing.setStatusOcorrencia(dto.getStatusOcorrencia());
                    existing.setRespostaSindico(dto.getRespostaSindico());
                    if ("RESOLVIDA".equals(dto.getStatusOcorrencia()) && existing.getDataResolucao() == null) {
                        existing.setDataResolucao(LocalDateTime.now());
                    } else if (dto.getDataResolucao() != null) { 
                        existing.setDataResolucao(dto.getDataResolucao());
                    }
                    if (imageFile != null && !imageFile.isEmpty()) {
                        String imageUrl = fileStorageService.storeFile(imageFile);
                        existing.setImageUrl(imageUrl);
                    }
                    if (dto.getMoradorId() != null) {
                        MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
                        existing.setMorador(morador);
                    }
                    OcorrenciaModel updated = repository.save(existing);
                    return ResponseEntity.ok(OcorrenciaMapper.toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar ocorrência: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarOcorrencia(@PathVariable Long id) {
        return repository.findById(id)
            .map(ocorrencia -> {
                ocorrencia.setStatusOcorrencia("CANCELADA");
                repository.save(ocorrencia);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}

