package medical.clinic.api.dto.consulta;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        Long idMedico,
        Long idPaciente,
        LocalDateTime data
) {
}
