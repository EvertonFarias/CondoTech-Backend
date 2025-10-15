package com.example.inovaTest.services;

import com.example.inovaTest.dtos.regras.RegraDTO;
import com.example.inovaTest.dtos.regras.RegraRequestDTO;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.models.RegraModel;
import com.example.inovaTest.repositories.CondominioRepository;
import com.example.inovaTest.repositories.RegraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegraService {

    // Injeção via construtor (melhor prática)
    private final RegraRepository regraRepository;
    private final CondominioRepository condominioRepository;
    private final UserContextService userContextService; // <-- INJEÇÃO DO NOVO SERVIÇO

    @Autowired
    public RegraService(RegraRepository regraRepository, 
                        CondominioRepository condominioRepository, 
                        UserContextService userContextService) {
        this.regraRepository = regraRepository;
        this.condominioRepository = condominioRepository;
        this.userContextService = userContextService;
    }

    @Transactional
    public RegraDTO create(RegraRequestDTO dto) {
        // Usando o serviço universal
        Long userCondominioId = userContextService.getCondominioIdOfCurrentUser();
        
        if (!userCondominioId.equals(dto.getCondominioId())) {
            throw new AccessDeniedException("Você não tem permissão para criar regras para este condomínio.");
        }

        CondominioModel condominio = condominioRepository.findById(userCondominioId)
            .orElseThrow(() -> new EntityNotFoundException("Condomínio não encontrado"));

        RegraModel novaRegra = new RegraModel();
        novaRegra.setTitulo(dto.getTitulo());
        novaRegra.setDescricao(dto.getDescricao());
        novaRegra.setCondominio(condominio);

        return new RegraDTO(regraRepository.save(novaRegra));
    }

    @Transactional(readOnly = true)
    public List<RegraDTO> findAll() {
        // Usando o serviço universal
        Long condominioId = userContextService.getCondominioIdOfCurrentUser();
        return regraRepository.findAllByCondominioId(condominioId).stream()
                .map(RegraDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RegraDTO update(Long id, RegraRequestDTO dto) {
        // Usando o serviço universal
        Long userCondominioId = userContextService.getCondominioIdOfCurrentUser();
        RegraModel regra = regraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Regra não encontrada com ID: " + id));

        if (!regra.getCondominio().getId().equals(userCondominioId)) {
            throw new AccessDeniedException("Você não tem permissão para editar esta regra.");
        }

        regra.setTitulo(dto.getTitulo());
        regra.setDescricao(dto.getDescricao());
        
        return new RegraDTO(regraRepository.save(regra));
    }

    @Transactional
    public void delete(Long id) {
        // Usando o serviço universal
        Long userCondominioId = userContextService.getCondominioIdOfCurrentUser();
        RegraModel regra = regraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Regra não encontrada com ID: " + id));
            
        if (!regra.getCondominio().getId().equals(userCondominioId)) {
            throw new AccessDeniedException("Você não tem permissão para excluir esta regra.");
        }
        
        regraRepository.deleteById(id);
    }
    
    // O método privado getCondominioIdFromUser foi REMOVIDO daqui.
}