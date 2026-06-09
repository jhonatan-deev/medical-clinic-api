package medical.clinic.api.dto.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import medical.clinic.api.enuns.Especialidade;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(
        Long medicoId,
        @NotNull
        Long pacienteId,
        @NotNull
        @Future
        LocalDateTime data,
        Especialidade especialidade
) {
}
