package medical.clinic.api.model;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter
public class TokenResetSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean used;

    public TokenResetSenha() {}

    public TokenResetSenha(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
        this.used = false;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }


    public void setUsed(boolean used) { this.used = used; }
}