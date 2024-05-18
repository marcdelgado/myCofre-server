package myCofre.server.service;

import myCofre.server.controller.dto.SignupRequest;
import myCofre.server.domain.User;
import myCofre.server.domain.Vault;
import myCofre.server.exceptions.DuplicateException;
import myCofre.server.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import myCofre.server.repository.VaultRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final VaultRepository vaultRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, VaultRepository vaultRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.vaultRepository = vaultRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void signup(SignupRequest request) {
    String email = request.email();
    Optional<User> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      throw new DuplicateException(String.format("User with the email address '%s' already exists.", email));
    }

    String hashedPassword = passwordEncoder.encode(request.password());
    User user = new User(request.name(), email, hashedPassword);
    userRepository.save(user);
    Vault vault = new Vault(user, "Default vault", "".getBytes(StandardCharsets.UTF_8));
    vaultRepository.save(vault);
  }

}
