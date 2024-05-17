package myCofre.server.repository;

import myCofre.server.domain.LoginAttempt;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
  @Query(value = "SELECT la FROM LoginAttempt la WHERE la.email = :email ORDER BY la.createdAt DESC")
  List<LoginAttempt> findRecent(@Param("email") String email, Pageable pageable);
}
/*
public class LoginAttemptRepository {

  private static final int RECENT_COUNT = 10; // can be in the config
  private static final String INSERT = "INSERT INTO login_attempt (email, success, created_at) VALUES(:email, :success, :createdAt)";
  private static final String FIND_RECENT = "SELECT * FROM login_attempt WHERE email = :email ORDER BY created_at DESC LIMIT :recentCount";

  private final JdbcClient jdbcClient;

  public LoginAttemptRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public void add(LoginAttempt loginAttempt) {
    long affected = jdbcClient.sql(INSERT)
        .param("email", loginAttempt.email())
        .param("success", loginAttempt.success())
        .param("createdAt", loginAttempt.createdAt())
        .update();

    Assert.isTrue(affected == 1, "Could not add login attempt.");
  }

  public List<LoginAttempt> findRecent(String email) {
    return jdbcClient.sql(FIND_RECENT)
        .param("email", email)
        .param("recentCount", RECENT_COUNT)
        .query(LoginAttempt.class)
        .list();
  }
}
*/