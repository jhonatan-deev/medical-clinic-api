package medical.clinic.api.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_confirmation_tokens")
@Getter
public class TokenConfirmacaoConta {
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

    private boolean usado;

    public TokenConfirmacaoConta() {}

    public TokenConfirmacaoConta(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.expiresAt = LocalDateTime.now().plusHours(24);
        this.usado = false;
    }

    public boolean isExpired() { return LocalDateTime.now().isAfter(this.expiresAt); }
    public void setUsado(boolean usado) { this.usado = usado; }
}
