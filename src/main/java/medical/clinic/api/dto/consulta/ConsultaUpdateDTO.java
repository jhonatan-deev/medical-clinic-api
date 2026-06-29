package medical.clinic.api.dto.consulta;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record ConsultaUpdateDTO(

        Long medicoId,
        @Future
        LocalDateTime data

) {
}