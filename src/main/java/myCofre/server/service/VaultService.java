package myCofre.server.service;

import myCofre.server.controller.vault.VaultRequest;
import myCofre.server.domain.Vault;
import myCofre.server.domain.User;
import myCofre.server.exceptions.OutOfSyncException;
import myCofre.server.repository.VaultRepository;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
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
    public Vault write(String username, VaultRequest vaultRequest) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            Optional<Vault> vault = vaultRepository.findByUserId(user.get().getId());
            if(vault.isPresent() && vault.get().getLastUpdateTimestamp().equals(vaultRequest.lastUpdateTimestamp()) ) {
                byte[] content = vaultRequest.vaultContent().getBytes(StandardCharsets.UTF_8);
                vault.get().setVaultContent(content);
                vault.get().setLastUpdateTimestamp(Timestamp.from(Instant.now()));
                return vaultRepository.save(vault.get());
            }else{
                throw new OutOfSyncException("Sent content is out of sync respect to server version");
            }
        }
        return null;
    }
}
