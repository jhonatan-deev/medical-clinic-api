package medical.clinic.api.dto.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import medical.clinic.api.dto.endereco.EnderecoDTO;
import medical.clinic.api.enuns.Especialidade;

public record MedicoRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "CRM é obrigatório")
        @Pattern(
                regexp = "\\d{4,6}",
                message = "CRM deve conter entre 4 e 6 números"
        )
        String crm,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(
                regexp = "\\d{11}",
                message = "Telefone deve conter 11 números"
        )
        String telefone,

        @NotNull(message = "Especialidade é obrigatória")
        Especialidade especialidade,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoDTO endereco
) {
}