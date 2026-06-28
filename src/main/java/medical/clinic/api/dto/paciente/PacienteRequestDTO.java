package medical.clinic.api.dto.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import medical.clinic.api.dto.endereco.EnderecoDTO;
import medical.clinic.api.dto.usuario.UsuarioRequestDTO;

public record PacienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(
                regexp = "\\d{11}",
                message = "Telefone deve conter 11 números"
        )
        String telefone,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(
                regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}",
                message = "CPF deve estar no formato 000.000.000-00"
        )
        String cpf,

        @Valid
        @NotNull(message = "Endereço é obrigatório")
        EnderecoDTO endereco,

        @Valid
        @NotNull(message = "Usuário é obrigatório")
        UsuarioRequestDTO usuario
) {}