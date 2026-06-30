package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.usuario.UsuarioRequestDTO;
import medical.clinic.api.dto.usuario.UsuarioUpdateSenhaDTO;
import medical.clinic.api.enuns.Perfil;
import medical.clinic.api.exception.DuplicateResourceException;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.mapper.UsuarioMapper;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario criarUsuario(UsuarioRequestDTO dto, Perfil perfil) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email já cadastrado.");
        }
        Usuario usuario = usuarioMapper.toEntity(dto, perfil);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(UsuarioUpdateSenhaDTO dto, Usuario usuarioLogado) {
        if (!passwordEncoder.matches(dto.senhaAtual(), usuarioLogado.getSenha())) {
            throw new RegraNegocioException("Senha atual incorreta.");
        }
        if(!dto.novaSenha().equals(dto.novaSenhaConfirmacao())){
            throw new RegraNegocioException("Senha e senha de confirmação devem ser iguais!");
        }
        if (passwordEncoder.matches(dto.novaSenha(), usuarioLogado.getSenha())) {
            throw new RegraNegocioException("A nova senha deve ser diferente da senha atual.");
        }
        Usuario.validarSenha(dto.novaSenha());
        usuarioLogado.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuarioLogado);
    }
}