package com.example.inovaTest.controllers;

import com.example.inovaTest.models.OcorrenciaModel;
import com.example.inovaTest.repositories.OcorrenciaRepository;
import com.example.inovaTest.services.FileStorageService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.List;

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

    // só para ADMIN 
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            String searchTerm = query.isEmpty() ? "" : "%" + query.toLowerCase() + "%";
            
            Page<OcorrenciaModel> pageResult = repository.searchAll(
                searchTerm, 
                status, 
                tipo, 
                pageable
            );

            return buildResponse(pageResult);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Apenas morador usa
    
    @GetMapping("/morador/{moradorId}/search")
    public ResponseEntity<Map<String, Object>> searchByMorador(
            @PathVariable Long moradorId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            String searchTerm = query.isEmpty() ? "" : "%" + query.toLowerCase() + "%";

            Page<OcorrenciaModel> pageResult = repository.searchByMorador(
                moradorId,
                searchTerm,
                status,
                tipo,
                pageable
            );

            return buildResponse(pageResult);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ MÉTODO AUXILIAR PARA CONSTRUIR RESPOSTA
    private ResponseEntity<Map<String, Object>> buildResponse(Page<OcorrenciaModel> pageResult) {
        List<OcorrenciaDTO> ocorrencias = pageResult.getContent().stream()
                .map(OcorrenciaMapper::toDTO)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("ocorrencias", ocorrencias);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("hasMore", pageResult.hasNext());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(OcorrenciaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<OcorrenciaDTO> create(
        @RequestPart("ocorrencia") String ocorrenciaJson,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
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

    @GetMapping("/morador/{moradorId}/abertas")
    public List<OcorrenciaDTO> findOpenByMorador(@PathVariable Long moradorId) {
        List<String> statusAbertos = List.of("ABERTA", "EM_ANDAMENTO");
        return repository.findByMoradorIdAndStatusOcorrenciaInOrderByCreatedAtDesc(moradorId, statusAbertos)
                .stream()
                .map(OcorrenciaMapper::toDTO)
                .toList();
    }

    @GetMapping("/count/morador/{moradorId}/abertas")
    public ResponseEntity<Map<String, Long>> countOpenByMorador(@PathVariable Long moradorId) {
        try {
            List<String> statusAbertos = List.of("ABERTA", "EM_ANDAMENTO");
            long count = repository.countByMoradorIdAndStatusOcorrenciaIn(moradorId, statusAbertos);
            
            Map<String, Long> response = new HashMap<>();
            response.put("ocorrenciasAbertas", count);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}