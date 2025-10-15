package com.example.inovaTest.services;

import com.example.inovaTest.dtos.areaComum.FotoAreaComumResponseDTO;
import com.example.inovaTest.models.AreaComumFotoModel;
import com.example.inovaTest.models.AreaComumModel;
import com.example.inovaTest.repositories.AreaComumFotoRepository;
import com.example.inovaTest.repositories.AreaComumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaComumFotoService {

    private static final int MAX_FOTOS_POR_AREA = 10;

    @Autowired
    private AreaComumFotoRepository fotoRepository;

    @Autowired
    private AreaComumRepository areaComumRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public FotoAreaComumResponseDTO addFoto(Long areaComumId, MultipartFile file, Boolean principal) {
        // Busca área comum
        AreaComumModel areaComum = areaComumRepository.findById(areaComumId)
            .orElseThrow(() -> new EntityNotFoundException("Área Comum não encontrada"));

        // Valida limite de fotos
        Long totalFotos = fotoRepository.countByAreaComumId(areaComumId);
        if (totalFotos >= MAX_FOTOS_POR_AREA) {
            throw new IllegalStateException("Limite de " + MAX_FOTOS_POR_AREA + " fotos atingido");
        }

        // Se for principal, remove a principal atual
        if (Boolean.TRUE.equals(principal)) {
            fotoRepository.findByAreaComumIdAndPrincipalTrue(areaComumId)
                .ifPresent(foto -> {
                    foto.setPrincipal(false);
                    fotoRepository.save(foto);
                });
        }

        // Salva arquivo físico
        String url = fileStorageService.storeAreaComumFoto(file, areaComum.getCondominio().getId());

        // Cria registro no banco
        AreaComumFotoModel foto = new AreaComumFotoModel();
        foto.setAreaComum(areaComum);
        foto.setUrl(url);
        foto.setNomeArquivo(file.getOriginalFilename());
        foto.setOrdem(totalFotos.intValue());
        foto.setPrincipal(Boolean.TRUE.equals(principal));

        AreaComumFotoModel fotoSalva = fotoRepository.save(foto);
        return new FotoAreaComumResponseDTO(fotoSalva);
    }

    @Transactional(readOnly = true)
    public List<FotoAreaComumResponseDTO> getFotosByAreaComum(Long areaComumId) {
        if (!areaComumRepository.existsById(areaComumId)) {
            throw new EntityNotFoundException("Área Comum não encontrada");
        }

        return fotoRepository.findByAreaComumIdOrderByOrdemAsc(areaComumId)
            .stream()
            .map(FotoAreaComumResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFoto(Long areaComumId, Long fotoId) {
        AreaComumFotoModel foto = fotoRepository.findByAreaComumIdAndId(areaComumId, fotoId)
            .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada"));

        // Deleta arquivo físico
        fileStorageService.deleteFile(foto.getUrl());

        // Deleta registro do banco
        fotoRepository.delete(foto);

        // Reorganiza ordem das fotos restantes
        reorganizarOrdem(areaComumId);
    }

    @Transactional
    public FotoAreaComumResponseDTO setPrincipal(Long areaComumId, Long fotoId) {
        // Remove principal atual
        fotoRepository.findByAreaComumIdAndPrincipalTrue(areaComumId)
            .ifPresent(foto -> {
                foto.setPrincipal(false);
                fotoRepository.save(foto);
            });

        // Define nova principal
        AreaComumFotoModel foto = fotoRepository.findByAreaComumIdAndId(areaComumId, fotoId)
            .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada"));
        
        foto.setPrincipal(true);
        AreaComumFotoModel fotoAtualizada = fotoRepository.save(foto);
        
        return new FotoAreaComumResponseDTO(fotoAtualizada);
    }

    @Transactional
    public List<FotoAreaComumResponseDTO> reordenarFotos(Long areaComumId, List<Long> novaOrdem) {
        List<AreaComumFotoModel> fotos = fotoRepository.findByAreaComumIdOrderByOrdemAsc(areaComumId);

        if (fotos.size() != novaOrdem.size()) {
            throw new IllegalArgumentException("Quantidade de fotos não corresponde");
        }

        for (int i = 0; i < novaOrdem.size(); i++) {
            Long fotoId = novaOrdem.get(i);
            AreaComumFotoModel foto = fotos.stream()
                .filter(f -> f.getId().equals(fotoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada: " + fotoId));
            
            foto.setOrdem(i);
            fotoRepository.save(foto);
        }

        return getFotosByAreaComum(areaComumId);
    }

    private void reorganizarOrdem(Long areaComumId) {
        List<AreaComumFotoModel> fotos = fotoRepository.findByAreaComumIdOrderByOrdemAsc(areaComumId);
        for (int i = 0; i < fotos.size(); i++) {
            fotos.get(i).setOrdem(i);
            fotoRepository.save(fotos.get(i));
        }
    }
}