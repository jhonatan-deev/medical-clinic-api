package medical.clinic.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

public record MedicoUpdateDTO(
        String nome,
        @Pattern(regexp = "\\d{11}", message = "Telefone deve ter 11 dígitos")
        String telefone,
        @Valid
        EnderecoDTO endereco
) {
}