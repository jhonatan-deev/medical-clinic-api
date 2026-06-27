package medical.clinic.api.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
