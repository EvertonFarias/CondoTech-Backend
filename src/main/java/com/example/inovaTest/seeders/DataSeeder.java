package com.example.inovaTest.seeders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.inovaTest.enums.GenderRole;
import com.example.inovaTest.enums.TipoMorador;
import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.models.CondominioModel;
import com.example.inovaTest.models.MoradorModel;
import com.example.inovaTest.models.UnidadeModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.models.VehicleModel; // <-- Importe o VehicleModel
import com.example.inovaTest.repositories.CondominioRepository;
import com.example.inovaTest.repositories.MoradorRepository;
import com.example.inovaTest.repositories.UnidadeRepository;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.repositories.VehicleRepository; // <-- Importe o VehicleRepository

@Component
@Order(1000)
public class DataSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CondominioRepository condominioRepository;
    private final UnidadeRepository unidadeRepository;
    private final MoradorRepository moradorRepository;
    private final VehicleRepository vehicleRepository; // <-- Injeção do novo repositório
    private boolean alreadySetup = false;

    public DataSeeder(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder,
        CondominioRepository condominioRepository,
        UnidadeRepository unidadeRepository,
        MoradorRepository moradorRepository,
        VehicleRepository vehicleRepository // <-- Injeção no construtor
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.condominioRepository = condominioRepository;
        this.unidadeRepository = unidadeRepository;
        this.moradorRepository = moradorRepository;
        this.vehicleRepository = vehicleRepository; // <-- Atribuição
    }

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (alreadySetup || userRepository.count() > 0) {
            System.out.println("Database already seeded, skipping...");
            return;
        }

        try {
            CondominioModel condominio = createCondominio();
            UnidadeModel unidadeAdmin = createUnidade(condominio, "302", "A");
            UnidadeModel unidadeUser = createUnidade(condominio, "105", "B");

            createAdminUser(unidadeAdmin);
            createRegularUser(unidadeUser);
            
            alreadySetup = true;
            System.out.println("Database seeding completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during database seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private CondominioModel createCondominio() {
        CondominioModel condominio = new CondominioModel();
        condominio.setNome("Condomínio Vila das Águas");
        condominio.setEndereco("Av. Rotary, 1234, Farol, Maceió - AL");
        condominio.setTelefone("(82) 3333-4444");
        condominio.setEmail("contato@viladasaguas.com");
        condominio.setCnpj("12.345.678/0001-90");
        return condominioRepository.save(condominio);
    }

    private UnidadeModel createUnidade(CondominioModel condominio, String numero, String bloco) {
        UnidadeModel unidade = new UnidadeModel();
        unidade.setCondominio(condominio);
        unidade.setNumero(numero);
        unidade.setBloco(bloco);
        unidade.setOcupada(true);
        return unidadeRepository.save(unidade);
    }

    private void createAdminUser(UnidadeModel unidade) {
        // Cria a conta de acesso (UserModel)
        UserModel adminUser = new UserModel();
        adminUser.setLogin("sindico@demo.com");
        adminUser.setPassword(passwordEncoder.encode("123456"));
        adminUser.setEmail("sindico@demo.com");
        adminUser.setRole(UserRole.ADMIN);
        adminUser.setEnabled(true);
        adminUser.setDateOfBirth(LocalDate.of(1985, 3, 15));
        adminUser.setGender(GenderRole.FEMALE);
        adminUser.setVerifiedEmail(true);
        userRepository.save(adminUser);

        // Cria o perfil de morador (MoradorModel)
        MoradorModel adminMorador = new MoradorModel();
        adminMorador.setUsuario(adminUser);
        adminMorador.setUnidade(unidade);
        adminMorador.setNome("Maria Silva (Síndica)");
        adminMorador.setTelefone("(11) 99999-1234");
        adminMorador.setEmail("sindico@demo.com");
        adminMorador.setTipo(TipoMorador.PROPRIETARIO);
        moradorRepository.save(adminMorador);

        // Cria os veículos e os vincula ao morador
        VehicleModel veiculo1 = new VehicleModel(null, adminMorador, "ABC-1234", "Honda", "Civic", "Preto");
        VehicleModel veiculo2 = new VehicleModel(null, adminMorador, "XYZ-5678", "Toyota", "Corolla", "Branco");
        vehicleRepository.saveAll(List.of(veiculo1, veiculo2));

        System.out.println("✓ Admin user, profile, and vehicles created: " + adminUser.getLogin());
    }

    private void createRegularUser(UnidadeModel unidade) {
        // Cria a conta de acesso (UserModel)
        UserModel regularUser = new UserModel();
        regularUser.setLogin("morador@demo.com");
        regularUser.setPassword(passwordEncoder.encode("123456"));
        regularUser.setEmail("morador@demo.com");
        regularUser.setRole(UserRole.USER);
        regularUser.setEnabled(true);
        regularUser.setDateOfBirth(LocalDate.of(1995, 7, 20));
        regularUser.setGender(GenderRole.MALE);
        regularUser.setVerifiedEmail(true);
        userRepository.save(regularUser);

        // Cria o perfil de morador (MoradorModel)
        MoradorModel regularMorador = new MoradorModel();
        regularMorador.setUsuario(regularUser);
        regularMorador.setUnidade(unidade);
        regularMorador.setNome("João Santos (Morador)");
        regularMorador.setTelefone("(11) 97777-4321");
        regularMorador.setEmail("morador@demo.com");
        regularMorador.setTipo(TipoMorador.INQUILINO);
        moradorRepository.save(regularMorador);

        // Cria o veículo e o vincula ao morador
        VehicleModel veiculoUser = new VehicleModel(null, regularMorador, "DEF-9876", "Fiat", "Mobi", "Vermelho");
        vehicleRepository.save(veiculoUser);

        System.out.println("✓ Regular user, profile, and vehicle created: " + regularUser.getLogin());
    }
}