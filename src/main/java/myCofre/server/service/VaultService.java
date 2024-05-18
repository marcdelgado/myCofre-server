package myCofre.server.service;

import myCofre.server.controller.dto.VaultRequest;
import myCofre.server.domain.Vault;
import myCofre.server.domain.User;
import myCofre.server.helper.SecurityUtils;
import myCofre.server.repository.VaultRepository;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

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
    public Vault findByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            Optional<Vault> vault = vaultRepository.findByUserId(user.get().getId());
            if (vault.isPresent()) return vault.get();
        }
        return null;
    }

    @Transactional
    public Vault updateVault(String username, VaultRequest updatedVaultRequest) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            Optional<Vault> vault = vaultRepository.findByUserId(user.get().getId());
            if(vault.isPresent()){
                byte[] content = updatedVaultRequest.content().getBytes(StandardCharsets.UTF_8);
                vault.get().setContent(content);
                return vaultRepository.save(vault.get());
            }
        }
        return null;
    }



}
