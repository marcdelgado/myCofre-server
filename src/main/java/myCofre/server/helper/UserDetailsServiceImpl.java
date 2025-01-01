package myCofre.server.helper;

import myCofre.server.repository.UserRepository;
import myCofre.server.domain.User;
import myCofre.server.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository repository;

  public UserDetailsServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {

    User user = repository.findByEmail(email).orElseThrow(() ->
        new NotFoundException(String.format("User does not exist, email: %s", email)));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getRepassword())
        .build();
  }
}
