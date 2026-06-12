package medical.clinic.api.validation.cancelamento;

import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.model.Consulta;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorConsultaNaoRealizada
        implements ValidadorCancelamentoConsulta {

    @Override
    public void validar(Consulta consulta) {

        if (consulta.getData().isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException(
                    "Não é possível cancelar uma consulta que já ocorreu!"
            );
        }
    }
}