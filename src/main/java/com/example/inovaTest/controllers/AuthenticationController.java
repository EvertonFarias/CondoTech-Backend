package com.example.inovaTest.controllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.Map;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inovaTest.dtos.auth.AuthenticationDTO;
import com.example.inovaTest.dtos.auth.ForgotPasswordDTO;
import com.example.inovaTest.dtos.auth.LoginResponseDTO;
import com.example.inovaTest.dtos.auth.RegisterDTO;
import com.example.inovaTest.dtos.auth.ResetPasswordDTO;
import com.example.inovaTest.dtos.user.UserResponseDTO;
import com.example.inovaTest.exceptions.ConflictException;
import com.example.inovaTest.infra.security.TokenService;
import com.example.inovaTest.models.EmailVerificationToken;
import com.example.inovaTest.models.PasswordResetToken;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.EmailVerificationTokenRepository;
import com.example.inovaTest.repositories.PasswordResetTokenRepository;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.services.AuthService;
import com.example.inovaTest.services.EmailService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailVerificationTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;
    @Value("${frontend.url}")
    private String frontendUrl;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @GetMapping("/verify") // rota para verificar o token
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido.");
        }

        EmailVerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expirado.");
        }

        UserModel user = verificationToken.getUser();
        user.setVerifiedEmail(true);
        userRepository.save(user); 

        tokenRepository.delete(verificationToken);

        return ResponseEntity.ok("E-mail verificado com sucesso. " + frontendUrl + "/auth/login");
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDTO dto) {
        System.out.println("📧 Solicitação de redefinição de senha recebida para o e-mail: " + dto.email());
        
        UserModel user = (UserModel) userRepository.findByEmail(dto.email());
        if (user == null) {
            System.out.println("❌ Usuário não encontrado: " + dto.email());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Usuário não encontrado"));
        }

        try {
            // Verificar se já existe um token para este usuário
            Optional<PasswordResetToken> existingToken = resetTokenRepository.findByUser(user);
            if (existingToken.isPresent()) {
                resetTokenRepository.delete(existingToken.get());
            }
            
            // Criar novo token
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user);
            resetTokenRepository.save(resetToken);

            // MUDANÇA AQUI: usar o esquema do app ao invés de URL web
            // String resetLink = "condotech://reset-password?token=" + token;
            String resetLink = "exp://192.168.18.127:8081/--/(auth)/reset-password?token=" + token;
            String htmlContent = emailService.loadResetPasswordTemplate(user.getLogin(), resetLink);
            
            emailService.sendEmail(user.getEmail(), "Redefinição de Senha", htmlContent);
            System.out.println("✅ E-mail enviado com sucesso");
            
            return ResponseEntity.ok(Map.of("message", "E-mail enviado com sucesso"));
            
        } catch (MessagingException e) {
            System.out.println("❌ Erro ao enviar e-mail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro ao enviar e-mail: " + e.getMessage()));
        } catch (IOException e) {
            System.out.println("❌ Erro ao carregar template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro ao carregar template: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro ao processar solicitação"));
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDTO dto) {
        System.out.println("🔐 Tentando redefinir senha com token: " + dto.token());
        
        Optional<PasswordResetToken> optionalToken = resetTokenRepository.findByToken(dto.token());
        if (optionalToken.isEmpty()) {
            System.out.println("❌ Token inválido ou não encontrado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Token inválido."));
        }

        PasswordResetToken token = optionalToken.get();
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            System.out.println("❌ Token expirado");
            resetTokenRepository.delete(token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Token expirado."));
        }

        try {
            UserModel user = token.getUser();
            user.setPassword(authService.encodePassword(dto.newPassword()));
            userRepository.save(user);
            resetTokenRepository.delete(token);
            System.out.println("✅ Senha redefinida com sucesso para usuário: " + user.getLogin());
            
            return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
        } catch (Exception e) {
            System.err.println("❌ Erro ao redefinir senha: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro ao redefinir senha: " + e.getMessage()));
        }
    }
}
