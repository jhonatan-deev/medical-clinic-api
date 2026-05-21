package medical.clinic.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import medical.clinic.api.enuns.Especialidade;

public record RequestDadosMedicoDTO(
        @NotBlank
        String nome,
        @Email(message = "Email inválido")
        String email,
        String crm,
        Especialidade especialidade,
        Endereco endereco
) {
}