package com.example.inovaTest.services;

import com.example.inovaTest.dtos.areaComum.*;
import com.example.inovaTest.models.AreaComumModel;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.models.RegraModel;
import com.example.inovaTest.repositories.AreaComumRepository;
import com.example.inovaTest.repositories.CondominioRepository;
import com.example.inovaTest.repositories.RegraRepository; // NOVO IMPORT
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AreaComumService {

    @Autowired
    private AreaComumRepository areaComumRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private RegraRepository regraRepository; 

    @Transactional
    public AreaComumDTO create(AreaComumRequestDTO dto) {
        CondominioModel condominio = condominioRepository.findById(dto.getCondominioId())
                .orElseThrow(() -> new EntityNotFoundException("Condomínio não encontrado"));

        AreaComumModel novaArea = new AreaComumModel();
        novaArea.setCondominio(condominio); // Define o condomínio primeiro
        mapDtoToModel(dto, novaArea);

        AreaComumModel areaSalva = areaComumRepository.save(novaArea);
        return new AreaComumDTO(areaSalva);
    }

    @Transactional(readOnly = true)
    public List<AreaComumDTO> findAll() {
        return areaComumRepository.findAll().stream().map(AreaComumDTO::new).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AreaComumDTO findById(Long id) {
        return areaComumRepository.findById(id).map(AreaComumDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Área Comum não encontrada com ID: " + id));
    }

    @Transactional
    public AreaComumDTO update(Long id, AreaComumRequestDTO dto) {
        AreaComumModel areaExistente = areaComumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área Comum não encontrada com ID: " + id));
        
        mapDtoToModel(dto, areaExistente);

        AreaComumModel areaAtualizada = areaComumRepository.save(areaExistente);
        return new AreaComumDTO(areaAtualizada);
    }

    @Transactional
    public void delete(Long id) {
        if (!areaComumRepository.existsById(id)) {
            throw new EntityNotFoundException("Área Comum não encontrada com ID: " + id);
        }
        areaComumRepository.deleteById(id);
    }

    // Mapeia os dados do DTO para o modelo, incluindo regras
  private void mapDtoToModel(AreaComumRequestDTO dto, AreaComumModel model) {
        model.setNome(dto.getNome());
        model.setDescricao(dto.getDescricao());
        model.setIcone(dto.getIcone());
        model.setAtiva(dto.getAtiva());
        model.setValorTaxa(dto.getValorTaxa());
        model.setTiposReservaDisponiveis(dto.getTiposReservaDisponiveis());
        Set<RegraModel> novasRegras = new HashSet<>();
        if (dto.getRegraIds() != null && !dto.getRegraIds().isEmpty()) {
            List<RegraModel> regrasEncontradas = regraRepository.findAllById(dto.getRegraIds());
            novasRegras.addAll(regrasEncontradas);
        }
        model.setRegras(novasRegras);
    }
}