package myCofre.server.service;

import myCofre.server.controller.dto.VaultRequest;
import myCofre.server.domain.Vault;
import myCofre.server.domain.User;
import myCofre.server.helper.SecurityUtils;
import myCofre.server.repository.VaultRepository;

import java.util.Objects;

import myCofre.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VaultService {

    private final VaultRepository vaultRepository;
    private final UserRepository userRepository;

    @Autowired
    public VaultService(VaultRepository vaultRepository, UserRepository userRepository) {
        this.vaultRepository = vaultRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Vault createVaultForUser(Long userId, String name, byte[] content) {
        // Encuentra el User por su ID
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Crea una nueva instancia de Chest y establece sus propiedades
        Vault vault = new Vault();
        vault.setUser(user);
        vault.setName(name);
        vault.setContent(content);

        // Guarda el Chest en la base de datos
        return vaultRepository.save(vault);
    }

    @Transactional
    public Vault createVaultForCurrentUser(VaultRequest req) {
        String username = Objects.requireNonNull(SecurityUtils.getCurrentUsername());

        // Encuentra el User por su ID
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Crea una nueva instancia de Chest y establece sus propiedades
        Vault vault = new Vault();
        vault.setUser(user);
        vault.setName(req.name());
        vault.setContent(req.content());

        // Guarda el Chest en la base de datos
        return vaultRepository.save(vault);
    }

}
