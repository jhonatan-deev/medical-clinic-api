package medical.clinic.api.dto.paciente;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf
) {
}
