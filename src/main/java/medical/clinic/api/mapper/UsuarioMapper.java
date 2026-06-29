package medical.clinic.api.mapper;

import medical.clinic.api.dto.usuario.UsuarioRequestDTO;
import medical.clinic.api.dto.usuario.UsuarioResponseDTO;
import medical.clinic.api.dto.usuario.UsuarioUpdateDTO;
import medical.clinic.api.enuns.Perfil;
import medical.clinic.api.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final PasswordEncoder passwordEncoder;

    public UsuarioMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario toEntity(UsuarioRequestDTO dto, Perfil perfil) {
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setPerfil(perfil);
        return usuario;
    }

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPerfil()
        );
    }

    public void toUpdate(Usuario usuario, UsuarioUpdateDTO dto) {
        if (dto.email() != null && !dto.email().isBlank()) {
            usuario.setEmail(dto.email());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }
    }
}