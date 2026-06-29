package medical.clinic.api.dto.usuario;

import medical.clinic.api.enuns.Perfil;

public record UsuarioResponseDTO(
        Long id,
        String email,
        Perfil perfil
) {
}
