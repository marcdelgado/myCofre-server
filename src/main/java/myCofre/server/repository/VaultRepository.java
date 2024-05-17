package myCofre.server.repository;

import myCofre.server.domain.Vault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaultRepository extends JpaRepository<Vault, Long> {
    Optional<Vault> findByUserId(Long userId);
    Optional<Vault> findByIdAndUserId(Long userId, Long id);
}
