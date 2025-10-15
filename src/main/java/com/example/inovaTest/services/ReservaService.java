package com.example.inovaTest.services;
import com.example.inovaTest.dtos.areaComum.AreaComumDTO;
import com.example.inovaTest.dtos.reserva.CreateReservaRequestDTO;
import com.example.inovaTest.dtos.reserva.ReservaDTO;
import com.example.inovaTest.enums.StatusReserva;
import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.models.AreaComumModel;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.models.ReservaModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.AreaComumRepository;
import com.example.inovaTest.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private AreaComumRepository areaComumRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public List<AreaComumDTO> getAllAreasComuns() {
        return areaComumRepository.findAll().stream()
                .map(AreaComumDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> getMinhasReservas(UserModel user) {
        if (user == null) {
            throw new RuntimeException("Usuário não autenticado.");
        }
        return reservaRepository.findByMoradorUsuarioId(user.getId()).stream()
                .map(ReservaDTO::new)
                .toList();
    }
    
    @Transactional
    public ReservaDTO createReserva(CreateReservaRequestDTO dto, UserModel user) {
        MoradorModel morador = user.getMorador();
        if (morador == null) {
            throw new RuntimeException("Apenas moradores podem criar reservas.");
        }

        AreaComumModel area = areaComumRepository.findById(dto.areaComumId())
                .orElseThrow(() -> new RuntimeException("Área comum não encontrada."));

        // Validação 1: Verificar se a área está ativa
        if (!area.isAtiva()) {
            throw new RuntimeException("Esta área não está disponível para reservas.");
        }

        // Validação 2: Verificar conflito de horários
        List<ReservaModel> reservasNoDia = reservaRepository.findByAreaComumIdAndDataReserva(area.getId(), dto.dataReserva());
        for (ReservaModel existente : reservasNoDia) {
            if (horariosConflitam(dto.horaInicio(), dto.horaFim(), existente.getHoraInicio(), existente.getHoraFim())) {
                throw new RuntimeException("Conflito de horário. O horário solicitado já está reservado.");
            }
        }

        ReservaModel novaReserva = new ReservaModel();
        novaReserva.setMorador(morador);
        novaReserva.setAreaComum(area);
        novaReserva.setDataReserva(dto.dataReserva());
        novaReserva.setHoraInicio(dto.horaInicio());
        novaReserva.setHoraFim(dto.horaFim());
        novaReserva.setStatus(StatusReserva.CONFIRMADA); // Ou PENDENTE, se precisar de aprovação

        ReservaModel reservaSalva = reservaRepository.save(novaReserva);
        return new ReservaDTO(reservaSalva);
    }

    @Transactional
    public void cancelarReserva(Long reservaId, UserModel user) {
        ReservaModel reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));

        if (!reserva.getMorador().getUsuario().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("Você não tem permissão para cancelar esta reserva.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    private boolean horariosConflitam(LocalTime inicioA, LocalTime fimA, LocalTime inicioB, LocalTime fimB) {
        // Verifica se (InicioA < FimB) e (FimA > InicioB)
        return inicioA.isBefore(fimB) && fimA.isAfter(inicioB);
    }
}