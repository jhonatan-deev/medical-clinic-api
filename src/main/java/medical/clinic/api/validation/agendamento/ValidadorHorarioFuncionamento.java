package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.exception.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamentoConsulta {

    @Override
    public void validar(ConsultaRequestDTO dados) {

        LocalDateTime dataHora = dados.data();

        if (dataHora == null) {
            throw new RegraNegocioException("Data/hora não informada");
        }

        DayOfWeek dia = dataHora.getDayOfWeek();
        int hora = dataHora.getHour();

        if (dia == DayOfWeek.SUNDAY) {
            throw new RegraNegocioException("Não é possível agendar consulta aos domingos!");
        }

        if (hora < 7) {
            throw new RegraNegocioException("Erro: só é possível agendar consulta depois das 7h!");
        }

        LocalDateTime termino = dataHora.plusHours(1);

        if (termino.getHour() > 19 || (termino.getHour() == 19 && termino.getMinute() > 0)) {
            throw new RegraNegocioException("Infelizmente essa consulta excede nosso horário de fechamento. A consulta deve terminar até às 19h!");
        }
    }
}