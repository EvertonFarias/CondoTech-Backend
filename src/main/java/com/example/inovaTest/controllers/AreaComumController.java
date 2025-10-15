package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.areaComum.*;
import com.example.inovaTest.services.AreaComumFotoService;
import com.example.inovaTest.services.AreaComumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/areas-comuns") 
@PreAuthorize("hasRole('ADMIN')") // Exige que o usuário tenha a ROLE_ADMIN
public class AreaComumController {

    @Autowired
    private AreaComumService areaComumService;

    @Autowired
    private AreaComumFotoService fotoService;

    @PostMapping
    public ResponseEntity<AreaComumDTO> create(@RequestBody @Valid AreaComumRequestDTO dto) {
        AreaComumDTO novaArea = areaComumService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaArea);
    }

    @GetMapping
    public ResponseEntity<List<AreaComumDTO>> findAll() {
        return ResponseEntity.ok(areaComumService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AreaComumDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(areaComumService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaComumDTO> update(@PathVariable Long id, @RequestBody @Valid AreaComumRequestDTO dto) {
        return ResponseEntity.ok(areaComumService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        areaComumService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    /**
     * Upload de foto para área comum
     * POST /admin/areas-comuns/{id}/fotos
     */
    @PostMapping(value = "/{areaComumId}/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoAreaComumResponseDTO> uploadFoto(
        @PathVariable Long areaComumId,
        @RequestParam("foto") MultipartFile file,
        @RequestParam(value = "principal", defaultValue = "false") Boolean principal
    ) {
        FotoAreaComumResponseDTO foto = fotoService.addFoto(areaComumId, file, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(foto);
    }

    /**
     * Lista todas as fotos de uma área comum
     * GET /admin/areas-comuns/{id}/fotos
     */
    @GetMapping("/{areaComumId}/fotos")
    public ResponseEntity<List<FotoAreaComumResponseDTO>> getFotos(@PathVariable Long areaComumId) {
        return ResponseEntity.ok(fotoService.getFotosByAreaComum(areaComumId));
    }

    /**
     * Remove uma foto
     * DELETE /admin/areas-comuns/{areaComumId}/fotos/{fotoId}
     */
    @DeleteMapping("/{areaComumId}/fotos/{fotoId}")
    public ResponseEntity<Void> deleteFoto(
        @PathVariable Long areaComumId,
        @PathVariable Long fotoId
    ) {
        fotoService.deleteFoto(areaComumId, fotoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Define uma foto como principal
     * PUT /admin/areas-comuns/{areaComumId}/fotos/{fotoId}/principal
     */
    @PutMapping("/{areaComumId}/fotos/{fotoId}/principal")
    public ResponseEntity<FotoAreaComumResponseDTO> setPrincipal(
        @PathVariable Long areaComumId,
        @PathVariable Long fotoId
    ) {
        FotoAreaComumResponseDTO foto = fotoService.setPrincipal(areaComumId, fotoId);
        return ResponseEntity.ok(foto);
    }

    /**
     * Reordena as fotos
     * PUT /admin/areas-comuns/{areaComumId}/fotos/reordenar
     * Body: [1, 3, 2, 4] (IDs na nova ordem)
     */
    @PutMapping("/{areaComumId}/fotos/reordenar")
    public ResponseEntity<List<FotoAreaComumResponseDTO>> reordenarFotos(
        @PathVariable Long areaComumId,
        @RequestBody List<Long> novaOrdem
    ) {
        List<FotoAreaComumResponseDTO> fotos = fotoService.reordenarFotos(areaComumId, novaOrdem);
        return ResponseEntity.ok(fotos);
    }
}


