package myCofre.server.domain;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String name;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50)
    private String surname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String repassword;

    @Column(nullable = false, length = 1)
    @Size(min = 1, max = 1)
    private String accountState;

    @Column(nullable = false)
    private Timestamp creationTimestamp;

    @Column(nullable = true)
    private Timestamp lastAccessTimestamp;

    @Column(nullable = true)
    private String activationToken;

    @Column(nullable = true)
    private String deleteToken;

    @Column(nullable = true)
    private Integer failAttempts;



    public User(String name, String surname, String email, String repassword, String activationToken){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.repassword = repassword;
        //
        this.accountState = "N"; //new
        this.creationTimestamp = Timestamp.from(Instant.now());
        this.creationTimestamp = Timestamp.from(Instant.now());
        this.lastAccessTimestamp = null;
        this.failAttempts = 0;
        this.activationToken = activationToken;
    }
}