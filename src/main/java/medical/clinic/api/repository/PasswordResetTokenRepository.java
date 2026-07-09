package medical.clinic.api.repository;

import medical.clinic.api.model.TokenResetSenha;
import medical.clinic.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<TokenResetSenha, Long> {
    Optional<TokenResetSenha> findByToken(String token);
    // Deleta todos os tokens de um usuário antes de criar um novo.
    // Evita acúmulo de tokens inválidos no banco.
    void deleteAllByUsuario(Usuario usuario);
}
