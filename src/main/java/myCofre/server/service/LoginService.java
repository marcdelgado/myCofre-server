package myCofre.server.service;

import myCofre.server.domain.LoginAttempt;
import myCofre.server.domain.User;
import myCofre.server.repository.LoginAttemptRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import myCofre.server.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginService {

  private final LoginAttemptRepository loginAttemptRepository;
  private final UserRepository userRepository;

  public LoginService(LoginAttemptRepository loginAttemptRepository, UserRepository userRepository) {
    this.loginAttemptRepository = loginAttemptRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void addLoginAttempt(String email, boolean success) {
    LoginAttempt loginAttempt = new LoginAttempt(email, success, LocalDateTime.now());
    loginAttemptRepository.save(loginAttempt);
  }

  public List<LoginAttempt> findRecentLoginAttempts(String email) {
    Pageable topTen = PageRequest.of(0, 10);
    return loginAttemptRepository.findRecent(email, topTen);
  }

  public User findUserByEmail(String email){
    Optional<User> user = userRepository.findByEmail(email);
    return user.orElse(null);
  }
}
