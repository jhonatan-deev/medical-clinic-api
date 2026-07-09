package medical.clinic.api.repository;

import medical.clinic.api.model.TokenConfirmacaoConta;
import medical.clinic.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfirmacaoContaRepository extends JpaRepository<TokenConfirmacaoConta, Long> {
    Optional<TokenConfirmacaoConta> findByToken(String token);
    void deleteAllByUsuario(Usuario usuario);
}
