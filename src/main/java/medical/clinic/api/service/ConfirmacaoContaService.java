package medical.clinic.api.service;

import medical.clinic.api.exception.TokenInvalidoException;
import medical.clinic.api.model.TokenConfirmacaoConta;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.repository.TokenConfirmacaoContaRepository;
import medical.clinic.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConfirmacaoContaService {

    private final UsuarioRepository usuarioRepository;
    private final TokenConfirmacaoContaRepository tokenRepository;
    private final EmailService emailService;

    public ConfirmacaoContaService(UsuarioRepository usuarioRepository,
                                   TokenConfirmacaoContaRepository tokenRepository,
                                   EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void enviarConfirmacao(Usuario usuario) {
        tokenRepository.deleteAllByUsuario(usuario);
        String token = UUID.randomUUID().toString();
        tokenRepository.save(new TokenConfirmacaoConta(token, usuario));
        emailService.enviarEmailDeConfirmacao(usuario.getEmail(), token);
    }

    @Transactional
    public void confirmarConta(String token) {
        TokenConfirmacaoConta confirmacaoToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Token inválido ou inexistente."));

        if (confirmacaoToken.isExpired()) {
            throw new TokenInvalidoException("Token expirado. Solicite um novo cadastro ou reenvio de confirmação.");
        }
        if (confirmacaoToken.isUsado()) {
            throw new TokenInvalidoException("Esta conta já foi confirmada.");
        }

        Usuario usuario = confirmacaoToken.getUsuario();
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        confirmacaoToken.setUsado(true);
        tokenRepository.save(confirmacaoToken);
    }
}