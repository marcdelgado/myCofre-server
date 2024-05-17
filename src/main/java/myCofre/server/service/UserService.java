package myCofre.server.service;

import myCofre.server.controller.dto.SignupRequest;
import myCofre.server.domain.User;
import myCofre.server.exceptions.DuplicateException;
import myCofre.server.repository.UserRepository;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void signup(SignupRequest request) {
    String email = request.email();
    Optional<User> existingUser = repository.findByEmail(email);
    if (existingUser.isPresent()) {
      throw new DuplicateException(String.format("User with the email address '%s' already exists.", email));
    }

    String hashedPassword = passwordEncoder.encode(request.password());
    User user = new User(request.name(), email, hashedPassword);
    repository.save(user);
  }

}
