package medical.clinic.api.dto.consulta;

import jakarta.validation.constraints.NotNull;
import medical.clinic.api.enuns.MotivoCancelamento;

public record CancelamentoRequestDTO(
        @NotNull
        Long idConsulta,
        @NotNull
        MotivoCancelamento motivoCancelamento
) {

}
