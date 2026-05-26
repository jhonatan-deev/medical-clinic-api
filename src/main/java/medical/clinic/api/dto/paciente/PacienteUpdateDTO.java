package medical.clinic.api.dto.paciente;
import jakarta.validation.Valid;
import medical.clinic.api.dto.endereco.EnderecoDTO;


public record PacienteUpdateDTO(
        String nome,
        String telefone,
        @Valid
        EnderecoDTO endereco
) {
}
