package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.usuario.UsuarioRequestDTO;
import medical.clinic.api.enuns.Perfil;
import medical.clinic.api.exception.DuplicateResourceException;
import medical.clinic.api.mapper.UsuarioMapper;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public Usuario criarUsuario(UsuarioRequestDTO dto, Perfil perfil) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email já cadastrado.");
        }

        Usuario usuario = usuarioMapper.toEntity(dto, perfil);
        return usuarioRepository.save(usuario);
    }
}