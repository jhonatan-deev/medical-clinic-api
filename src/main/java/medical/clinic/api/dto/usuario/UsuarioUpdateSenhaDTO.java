package medical.clinic.api.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateSenhaDTO(
        @NotBlank
        String senhaAtual,
        @NotBlank
        String novaSenha,
        @NotBlank
        String novaSenhaConfirmacao

) {
}
