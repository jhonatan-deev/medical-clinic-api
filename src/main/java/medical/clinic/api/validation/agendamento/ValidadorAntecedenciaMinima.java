package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.exception.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaMinima implements ValidadorAgendamentoConsulta {

    @Override
    public void validar(ConsultaRequestDTO dados) {

        var horaLimite = LocalDateTime.now().plusMinutes(30);

        if (dados.data().isBefore(horaLimite)) {
            throw new RegraNegocioException("Consulta deve ser agendada com pelo menos 30 minutos de antecedência!");
        }
    }
}