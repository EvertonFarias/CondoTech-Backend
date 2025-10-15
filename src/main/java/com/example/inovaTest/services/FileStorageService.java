package com.example.inovaTest.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return "/uploads/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Armazena arquivo de foto de área comum em diretório específico do condomínio
     * Cria estrutura: uploads/condominio_{id}/areas-comuns/
     */
    public String storeAreaComumFoto(MultipartFile file, Long condominioId) {
        // Validações
        validateImageFile(file);
        
        // Cria estrutura de diretórios
        Path condominioDir = fileStorageLocation.resolve("condominio_" + condominioId).resolve("areas-comuns");
        
        try {
            Files.createDirectories(condominioDir);
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível criar o diretório para as fotos.", ex);
        }

        // Gera nome único
        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "." + extension;

        try {
            Path targetLocation = condominioDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            
            // Retorna URL relativa
            return "/uploads/condominio_" + condominioId + "/areas-comuns/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao salvar arquivo " + fileName, ex);
        }
    }

    /**
     * Deleta arquivo físico
     */
    public void deleteFile(String fileUrl) {
        try {
            // Remove a barra inicial da URL
            String relativePath = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;
            Path filePath = Paths.get(relativePath).toAbsolutePath().normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao deletar arquivo: " + fileUrl, ex);
        }
    }

    /**
     * Valida se o arquivo é uma imagem válida
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 5MB");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Use: jpg, jpeg, png, gif ou webp");
        }
    }

    /**
     * Extrai extensão do arquivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Nome de arquivo inválido");
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}