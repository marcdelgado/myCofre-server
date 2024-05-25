package myCofre.server.service;

import myCofre.server.controller.user.ActivateRequest;
import myCofre.server.controller.user.DeleteRequest;
import myCofre.server.controller.user.SignupRequest;
import myCofre.server.controller.user.UserRequest;
import myCofre.server.domain.User;
import myCofre.server.domain.Vault;
import myCofre.server.exceptions.AccessDeniedException;
import myCofre.server.exceptions.DuplicateException;
import myCofre.server.exceptions.NotFoundException;
import myCofre.server.helper.TokenHelper;
import myCofre.server.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import myCofre.server.repository.VaultRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService{

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
    String hashedPassword = passwordEncoder.encode(request.repassword());
    String activationToken = TokenHelper.generateHumanToken(6);
    User user = new User(request.name(), request.surname(), email, hashedPassword, activationToken);
    userRepository.save(user);
    Vault vault = new Vault(user, "Default vault", "".getBytes(StandardCharsets.UTF_8), Timestamp.from(Instant.now()));
    vaultRepository.save(vault);
  }

  @Transactional
  public User view(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user.orElse(null);
  }

  @Transactional
  public void edit(String previousEmail, UserRequest userRequest) {
    //IF user changes email, verify it's unique.
    if(!previousEmail.equals(userRequest.email())){
      Optional<User> existingUser = userRepository.findByEmail(userRequest.email());
      if (existingUser.isPresent()) {
        throw new DuplicateException(String.format("User with the email address '%s' already exists.", userRequest.email()));
      }
    }
    User user = view(previousEmail);
    user.setName(userRequest.name());
    user.setSurname(userRequest.surname());
    user.setEmail(userRequest.email());
    userRepository.save(user);
  }

  @Transactional
  public void activate(ActivateRequest activateRequest) {
    Optional<User> existingUser = userRepository.findByEmail(activateRequest.email());
    if (existingUser.isEmpty()) {
      throw new NotFoundException("This email address is not registered.");
    }else{
      String storedToken = existingUser.get().getActivationToken();
      if(storedToken.equals(activateRequest.activationToken())){
        User user = view(activateRequest.email());
        user.setAccountState("A");
        user.setActivationToken(null);
        userRepository.save(user);
      }else{
        throw new AccessDeniedException("This token is not valid for activate this account.");
      }
    }
  }

  @Transactional
  public void requestDelete(DeleteRequest deleteRequest) {
    Optional<User> user = userRepository.findByEmail(deleteRequest.email());
    if (user.isEmpty()) {
      throw new NotFoundException("This email address is not registered.");
    }else{
      String deleteToken = TokenHelper.generateHumanToken(6);
      user.get().setDeleteToken(deleteToken);
      userRepository.save(user.get());
    }
  }

  @Transactional
  public void confirmDelete(DeleteRequest deleteRequest) {
    Optional<User> existingUser = userRepository.findByEmail(deleteRequest.email());
    if (existingUser.isEmpty()) {
      throw new NotFoundException("This email address is not registered.");
    }
    String storedToken = existingUser.get().getDeleteToken();
    if(storedToken.equals(deleteRequest.deleteToken())){
      User user = view(deleteRequest.email());
      Optional<Vault> vault = vaultRepository.findByUserId(user.getId());
      vault.ifPresent(vaultRepository::delete);
      userRepository.delete(user);
    }else{
      throw new AccessDeniedException("This token is not valid for activate this account.");
    }
  }

}
