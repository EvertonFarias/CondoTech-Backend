package com.example.inovaTest.services;

import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    /**
     * Obtém o objeto UserModel completo do usuário atualmente autenticado.
     *
     * @return O UserModel do usuário logado.
     * @throws IllegalStateException se não houver usuário autenticado ou o tipo for inesperado.
     */
    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Nenhum usuário autenticado encontrado no contexto de segurança.");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserModel) {
            return (UserModel) principal;
        }
        throw new IllegalStateException("O principal de autenticação não é uma instância de UserModel.");
    }

    /**
     * Obtém o ID do Condomínio associado ao usuário atualmente autenticado.
     * Esta é a sua função universal.
     *
     * @return O ID do condomínio.
     * @throws IllegalStateException se o usuário não estiver associado a um condomínio válido.
     */
    public Long getCondominioIdOfCurrentUser() {
        UserModel currentUser = getCurrentUser();
        MoradorModel morador = currentUser.getMorador();
        
        if (morador == null || morador.getUnidade() == null || morador.getUnidade().getCondominio() == null) {
            throw new IllegalStateException("O usuário autenticado não está associado a um condomínio válido.");
        }
        
        return morador.getUnidade().getCondominio().getId();
    }
}