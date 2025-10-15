package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.comentario.ComentarioDTO;
import com.example.inovaTest.mappers.ComentarioMapper;
import com.example.inovaTest.models.ComentarioModel;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.models.OcorrenciaModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.ComentarioRepository;
import com.example.inovaTest.repositories.MoradorRepository;
import com.example.inovaTest.repositories.OcorrenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {
    
    @Autowired
    private ComentarioRepository comentarioRepository;
    
    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;
    
    @Autowired
    private MoradorRepository moradorRepository;
    
    @GetMapping("/ocorrencia/{ocorrenciaId}")
    public ResponseEntity<List<ComentarioDTO>> getComentariosByOcorrencia(@PathVariable Long ocorrenciaId) {
        List<ComentarioModel> comentarios = comentarioRepository.findByOcorrenciaIdOrderByCreatedAtAsc(ocorrenciaId);
        List<ComentarioDTO> dtos = comentarios.stream()
                .map(ComentarioMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping
    public ResponseEntity<ComentarioDTO> createComentario(@RequestBody ComentarioDTO dto) {
        try {
            // Busca a ocorrência
            OcorrenciaModel ocorrencia = ocorrenciaRepository.findById(dto.getOcorrenciaId())
                    .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));
            
            // Busca o morador
            MoradorModel morador = moradorRepository.findById(dto.getMoradorId())
                    .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
            
            ComentarioModel comentario = ComentarioMapper.toModel(dto);
            comentario.setOcorrencia(ocorrencia);
            comentario.setMorador(morador);
            
            // Verifica se o morador tem usuário associado e se é admin
            if (morador.getUsuario() != null) {
                UserModel usuario = morador.getUsuario();
                comentario.setIsAdmin(usuario.getRole().name().equals("ADMIN"));
            } else {
                comentario.setIsAdmin(false);
            }
            
            ComentarioModel saved = comentarioRepository.save(comentario);
            return ResponseEntity.ok(ComentarioMapper.toDTO(saved));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar comentário: " + e.getMessage(), e);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        return comentarioRepository.findById(id)
                .map(comentario -> {
                    comentarioRepository.delete(comentario);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}