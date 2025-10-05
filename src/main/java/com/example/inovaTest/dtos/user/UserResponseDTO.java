package com.example.inovaTest.dtos.user;

import com.example.inovaTest.dtos.vehicle.VehicleDTO;
import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.models.UserModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponseDTO(
    // Dados do UserModel (autenticação)
    UUID id,
    String login,
    String email,
    UserRole role,
    // Dados do MoradorModel (perfil)
    Long moradorId,
    String name,
    String phone,
    String unit,
    String tower,
    
    // Lista de Veículos
    List<VehicleDTO> vehicles,

    // Dados Derivados
    String avatar
) {
    /**
     * Construtor que mapeia um UserModel completo para o DTO que será enviado ao front-end.
     * @param user A entidade UserModel recuperada do banco de dados.
     */
    public UserResponseDTO(UserModel user) {
        this(
            // Dados diretos do UserModel
            user.getId(),
            user.getLogin(),
            user.getEmail(),
            user.getRole(),

            
            (user.getMorador() != null) 
                ? user.getMorador().getId()
                : null, 
            
            (user.getMorador() != null) 
                ? user.getMorador().getNome() 
                : user.getLogin(), // Fallback para o login se não houver nome
            
            (user.getMorador() != null) 
                ? user.getMorador().getTelefone() 
                : null,
            
            (user.getMorador() != null && user.getMorador().getUnidade() != null) 
                ? user.getMorador().getUnidade().getNumero() 
                : null,
            
            (user.getMorador() != null && user.getMorador().getUnidade() != null) 
                ? user.getMorador().getUnidade().getBloco() 
                : null,

            // Mapeia a lista de VehicleModel para uma lista de VehicleDTO
            (user.getMorador() != null && user.getMorador().getVeiculos() != null)
                ? user.getMorador().getVeiculos().stream().map(VehicleDTO::new).collect(Collectors.toList())
                : Collections.emptyList(),

            // Gera as iniciais para o avatar a partir do nome do morador
            (user.getMorador() != null && user.getMorador().getNome() != null && !user.getMorador().getNome().isEmpty())
                ? Arrays.stream(user.getMorador().getNome().split(" "))
                        .map(s -> String.valueOf(s.charAt(0)))
                        .collect(Collectors.joining())
                : ""
        );
    }
}