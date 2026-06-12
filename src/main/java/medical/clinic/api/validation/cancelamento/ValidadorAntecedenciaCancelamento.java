package medical.clinic.api.validation.cancelamento;

import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.model.Consulta;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaCancelamento
        implements ValidadorCancelamentoConsulta {

    @Override
    public void validar(Consulta consulta) {

        LocalDateTime limiteCancelamento =
                LocalDateTime.now().plusHours(24);

        if (consulta.getData().isBefore(limiteCancelamento)) {
            throw new RegraNegocioException(
                    "Cancelamento só pode ser feito com 24 horas de antecedência!"
            );
        }
    }
}