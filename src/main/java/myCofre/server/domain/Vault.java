package myCofre.server.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Vault", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class Vault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = true)
    private Timestamp lastUpdateTimestamp;

    @Lob
    @Column(nullable = false)
    @Size(max = 1048576) // Limita el tamaño del contenido a 1MB
    private byte[] vaultContent;

    public Vault(User user, String name, byte[] vaultContent, Timestamp lastUpdateTimestamp){
        this.user = user;
        this.name = name;
        this.vaultContent = vaultContent;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }
}
